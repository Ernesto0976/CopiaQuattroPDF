package com.copsis.models.impresion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.controllers.forms.UrlForm;
import com.copsis.models.EstructuraAseguradosModel;

import ch.qos.logback.core.util.Loader;

import java.util.Map;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;

import java.util.Collection;
import java.util.Collections;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSArray;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.FileOutputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfImageObject;

public class ImpresionConsolidadoModelPdf {
	public static final float FACTOR = 0.5f;

	public byte[] buildPDF(ImpresionForm impresionForm) {
		byte[] pdfArray = null;
		PDFMergerUtility PDFmerger = new PDFMergerUtility();
		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {

					List<UrlForm> list = impresionForm.getUrls();

					list.sort(Comparator.comparing(UrlForm::getOrden));

					for (UrlForm urlForm : list) {

						URL scalaByExampleUrl = new URL(urlForm.getUrl());

						final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());

						PDFmerger.appendDocument(document, documentToBeParsed);
					}
					PDFmerger.mergeDocuments();

					output = new ByteArrayOutputStream();

					final Path path = Files.createTempFile("consolidado", ".pdf");
					final Path path2 = Files.createTempFile("consolidado2", ".pdf");

					document.save(output);

					Files.write(path, output.toByteArray());
			

					manipulatePdf(path.toString(), path2.toString());

					File ruta = path2.toFile();

					byte[] fileContent = Files.readAllBytes(ruta.toPath());

					path.toFile().deleteOnExit();
					path2.toFile().deleteOnExit();
					return fileContent;
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			System.out.println("Error en la impresion de Sio4CertificadoInterPdf  ==> " + ex.getMessage());
			return pdfArray;
		}

	}

	public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
		PdfReader reader = new PdfReader(src);
		int n = reader.getXrefSize();
		PdfObject object;
		PRStream stream;
		// Look for image and manipulate image stream
		for (int i = 0; i < n; i++) {
			object = reader.getPdfObject(i);
			if (object == null || !object.isStream())
				continue;
			stream = (PRStream) object;
			if (!PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE)))
				continue;
			if (!PdfName.DCTDECODE.equals(stream.getAsName(PdfName.FILTER)))
				continue;
			PdfImageObject image = new PdfImageObject(stream);
			BufferedImage bi = image.getBufferedImage();
			if (bi == null)
				continue;
			int width = (int) (bi.getWidth() * FACTOR);
			int height = (int) (bi.getHeight() * FACTOR);
			if (width <= 0 || height <= 0)
				continue;
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
			Graphics2D g = img.createGraphics();
			g.drawRenderedImage(bi, at);
			ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
			ImageIO.write(img, "JPG", imgBytes);
			stream.clear();
			stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
			stream.put(PdfName.TYPE, PdfName.XOBJECT);
			stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
			stream.put(PdfName.FILTER, PdfName.DCTDECODE);
			stream.put(PdfName.WIDTH, new PdfNumber(width));
			stream.put(PdfName.HEIGHT, new PdfNumber(height));
			stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
			stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
		}
		reader.removeUnusedObjects();
		// Save altered PDF
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.setFullCompression();
		stamper.close();
		reader.close();
	}

	public void testOptimizeDummy(PDDocument doc) throws IOException {
		try (InputStream resource = getClass().getResourceAsStream("original13.pdf")) {

			// URL scalaByExampleUrl = new URL(urlForm.getUrl());
			// PDDocument pdDocument = PDDocument.load(scalaByExampleUrl.openStream());
			PDDocument pdDocument = doc;

			optimize(pdDocument);

			// pdDocument.save(new File("/home/development/Videos/original3.pdf"));
		}
	}

	public void optimize(PDDocument pdDocument) throws IOException {
		Map<COSBase, Collection<Reference>> complexObjects = findComplexObjects(pdDocument);
		for (int pass = 0;; pass++) {
			int merges = mergeDuplicates(complexObjects);
			if (merges <= 0) {
				System.out.printf("Pass %d - No merged objects\n\n", pass);
				break;
			}
			System.out.printf("Pass %d - Merged objects: %d\n\n", pass, merges);
		}
	}

	Map<COSBase, Collection<Reference>> findComplexObjects(PDDocument pdDocument) {
		COSDictionary catalogDictionary = pdDocument.getDocumentCatalog().getCOSObject();
		Map<COSBase, Collection<Reference>> incomingReferences = new HashMap<>();
		incomingReferences.put(catalogDictionary, new ArrayList<>());

		Set<COSBase> lastPass = Collections.<COSBase>singleton(catalogDictionary);
		Set<COSBase> thisPass = new HashSet<>();
		while (!lastPass.isEmpty()) {
			for (COSBase object : lastPass) {
				if (object instanceof COSArray) {
					COSArray array = (COSArray) object;
					for (int i = 0; i < array.size(); i++) {
						addTarget(new ArrayReference(array, i), incomingReferences, thisPass);
					}
				} else if (object instanceof COSDictionary) {
					COSDictionary dictionary = (COSDictionary) object;
					for (COSName key : dictionary.keySet()) {
						addTarget(new DictionaryReference(dictionary, key), incomingReferences, thisPass);
					}
				}
			}
			lastPass = thisPass;
			thisPass = new HashSet<>();
		}
		return incomingReferences;
	}

	static class DictionaryReference implements Reference {
		public DictionaryReference(COSDictionary dictionary, COSName key) {
			this.from = dictionary;
			this.key = key;
		}

		@Override
		public COSBase getFrom() {
			return from;
		}

		@Override
		public COSBase getTo() {
			return resolve(from.getDictionaryObject(key));
		}

		@Override
		public void setTo(COSBase to) {
			from.setItem(key, to);
		}

		final COSDictionary from;
		final COSName key;
	}

	interface Reference {
		public COSBase getFrom();

		public COSBase getTo();

		public void setTo(COSBase to);
	}

	static COSBase resolve(COSBase object) {
		while (object instanceof COSObject)
			object = ((COSObject) object).getObject();
		return object;
	}

	static class ArrayReference implements Reference {
		public ArrayReference(COSArray array, int index) {
			this.from = array;
			this.index = index;
		}

		@Override
		public COSBase getFrom() {
			return from;
		}

		@Override
		public COSBase getTo() {
			return resolve(from.get(index));
		}

		@Override
		public void setTo(COSBase to) {
			from.set(index, to);
		}

		final COSArray from;
		final int index;
	}

	void addTarget(Reference reference, Map<COSBase, Collection<Reference>> incomingReferences, Set<COSBase> thisPass) {
		COSBase object = reference.getTo();
		if (object instanceof COSArray || object instanceof COSDictionary) {
			Collection<Reference> incoming = incomingReferences.get(object);
			if (incoming == null) {
				incoming = new ArrayList<>();
				incomingReferences.put(object, incoming);
				thisPass.add(object);
			}
			incoming.add(reference);
		}
	}

	int mergeDuplicates(Map<COSBase, Collection<Reference>> complexObjects) throws IOException {
		List<HashOfCOSBase> hashes = new ArrayList<>(complexObjects.size());
		for (COSBase object : complexObjects.keySet()) {
			hashes.add(new HashOfCOSBase(object));
		}
		Collections.sort(hashes);

		int removedDuplicates = 0;
		if (!hashes.isEmpty()) {
			int runStart = 0;
			int runHash = hashes.get(0).hash;
			for (int i = 1; i < hashes.size(); i++) {
				int hash = hashes.get(i).hash;
				if (hash != runHash) {
					int runSize = i - runStart;
					if (runSize != 1) {
						System.out.printf("Equal hash %d for %d elements.\n", runHash, runSize);
						removedDuplicates += mergeRun(complexObjects, hashes.subList(runStart, i));
					}
					runHash = hash;
					runStart = i;
				}
			}
			int runSize = hashes.size() - runStart;
			if (runSize != 1) {
				System.out.printf("Equal hash %d for %d elements.\n", runHash, runSize);
				removedDuplicates += mergeRun(complexObjects, hashes.subList(runStart, hashes.size()));
			}
		}
		return removedDuplicates;
	}

	static class HashOfCOSBase implements Comparable<HashOfCOSBase> {
		public HashOfCOSBase(COSBase object) throws IOException {
			this.object = object;
			this.hash = calculateHash(object);
		}

		int calculateHash(COSBase object) throws IOException {
			if (object instanceof COSArray) {
				int result = 1;
				for (COSBase member : (COSArray) object)
					result = 31 * result + member.hashCode();
				return result;
			} else if (object instanceof COSDictionary) {
				int result = 3;
				for (Map.Entry<COSName, COSBase> entry : ((COSDictionary) object).entrySet())
					result += entry.hashCode();
				if (object instanceof COSStream) {
					try (InputStream data = ((COSStream) object).createRawInputStream()) {
						MessageDigest md = MessageDigest.getInstance("MD5");
						byte[] buffer = new byte[8192];
						int bytesRead = 0;
						while ((bytesRead = data.read(buffer)) >= 0)
							md.update(buffer, 0, bytesRead);
						result = 31 * result + Arrays.hashCode(md.digest());
					} catch (NoSuchAlgorithmException e) {
						throw new IOException(e);
					}
				}
				return result;
			} else {
				throw new IllegalArgumentException(
						String.format("Unknown complex COSBase type %s", object.getClass().getName()));
			}
		}

		final COSBase object;
		final int hash;

		@Override
		public int compareTo(HashOfCOSBase o) {
			int result = Integer.compare(hash, o.hash);
			if (result == 0)
				result = Integer.compare(hashCode(), o.hashCode());
			return result;
		}
	}

	int mergeRun(Map<COSBase, Collection<Reference>> complexObjects, List<HashOfCOSBase> run) {
		int removedDuplicates = 0;

		List<List<COSBase>> duplicateSets = new ArrayList<>();
		for (HashOfCOSBase entry : run) {
			COSBase element = entry.object;
			for (List<COSBase> duplicateSet : duplicateSets) {
				if (equals(element, duplicateSet.get(0))) {
					duplicateSet.add(element);
					element = null;
					break;
				}
			}
			if (element != null) {
				List<COSBase> duplicateSet = new ArrayList<>();
				duplicateSet.add(element);
				duplicateSets.add(duplicateSet);
			}
		}

		System.out.printf("Identified %d set(s) of identical objects in run.\n", duplicateSets.size());

		for (List<COSBase> duplicateSet : duplicateSets) {
			if (duplicateSet.size() > 1) {
				COSBase surviver = duplicateSet.remove(0);
				Collection<Reference> surviverReferences = complexObjects.get(surviver);
				for (COSBase object : duplicateSet) {
					Collection<Reference> references = complexObjects.get(object);
					for (Reference reference : references) {
						reference.setTo(surviver);
						surviverReferences.add(reference);
					}
					complexObjects.remove(object);
					removedDuplicates++;
				}
				surviver.setDirect(false);
			}
		}

		return removedDuplicates;
	}

	boolean equals(COSBase a, COSBase b) {
		if (a instanceof COSArray) {
			if (b instanceof COSArray) {
				COSArray aArray = (COSArray) a;
				COSArray bArray = (COSArray) b;
				if (aArray.size() == bArray.size()) {
					for (int i = 0; i < aArray.size(); i++) {
						if (!resolve(aArray.get(i)).equals(resolve(bArray.get(i))))
							return false;
					}
					return true;
				}
			}
		} else if (a instanceof COSDictionary) {
			if (b instanceof COSDictionary) {
				COSDictionary aDict = (COSDictionary) a;
				COSDictionary bDict = (COSDictionary) b;
				Set<COSName> keys = aDict.keySet();
				// As proposed by jchobantonov on github, we can compare dictionary sizes
				// here instead of the dictionaries themselves as we compare the values
				// key by key in the body of the if.
				if (keys.size() == bDict.keySet().size()) {
					for (COSName key : keys) {
						if (!resolve(aDict.getItem(key)).equals(bDict.getItem(key)))
							return false;
					}
					// In case of COSStreams we strictly speaking should
					// also compare the stream contents here. But apparently
					// their hashes coincide well enough for the original
					// hashing equality, so let's just assume...
					return true;
				}
			}
		}
		return false;
	}

}
