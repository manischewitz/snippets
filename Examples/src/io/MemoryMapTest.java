package io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.LongSupplier;
import java.util.function.ToLongFunction;
import java.util.zip.CRC32;

/**
 * This program computes the CRC checksum of a file in four ways.
 **/
public class MemoryMapTest {

	private static long checksumInputStream (Path filename) throws IOException {
		try (InputStream in = Files.newInputStream(filename)) {
			CRC32 crc32 = new CRC32();
			int c;
			while ((c = in.read()) != -1) {
				crc32.update(c);
			}
			return crc32.getValue();
		}
	}
	
	private static long checksumBufferedInputStream (Path filename) throws IOException {
		try (InputStream in = 
				new BufferedInputStream(Files.newInputStream(filename))) {
			CRC32 crc32 = new CRC32();
			int c;
			while ((c = in.read()) != -1) {
				crc32.update(c);
			}
			return crc32.getValue();
		}
	}
	
	private static long checksumRandomAccessFile(Path filename) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(filename.toFile(), "r")) {
			long length = raf.length();
			CRC32 crc32 = new CRC32();
			for (long p = 0; p < length; p++) {
				raf.seek(p);
				int c = raf.readByte();
				crc32.update(c);
			}
			return crc32.getValue();
		}
	}
	
	private static long checksumMappedFile(Path filename) throws IOException {
		try (FileChannel channel = FileChannel.open(filename)) {
			CRC32 crc32 = new CRC32();
			int length = (int) channel.size(); 
			MappedByteBuffer mbb = channel.map(
					FileChannel.MapMode.READ_ONLY, 0, length);
			for (int p = 0; p < length; p++) {
				int c = mbb.get(p);
				crc32.update(c);
			}
			return crc32.getValue();
		}
	}
	
	public static void main(String[] args) throws IOException {
		String path = "";
		try (Scanner s = new Scanner(System.in)) {
			System.out.println("File to process: ");
			path = s.nextLine();
		}
		final Path fileName = Paths.get(path.equals("") ? "alice30.txt" : path);
		test(() -> {
			try {
				System.out.println("Input Stream:");
				return checksumInputStream(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		});
		test(() -> {
			try {
				System.out.println("Buffered Input Stream:");
				return checksumBufferedInputStream(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		});
		test(() -> {
			try {
				System.out.println("Random Access File:");
				return checksumRandomAccessFile(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		});
		test(() -> {
			try {
				System.out.println("Mapped File:");
				return checksumMappedFile(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		});
	}
	
	private static void test(LongSupplier sup) {
		long start = System.currentTimeMillis();
		long crc32Value = sup.getAsLong();
		long end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crc32Value));
		System.out.println((end - start) + " milliseconds");
	}

}
