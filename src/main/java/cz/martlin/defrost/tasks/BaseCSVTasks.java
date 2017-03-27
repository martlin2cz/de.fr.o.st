package cz.martlin.defrost.tasks;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import cz.martlin.defrost.misc.DefrostException;

public class BaseCSVTasks {

	public static final CSVFormat FORMAT = CSVFormat.DEFAULT;

	private BaseCSVTasks() {
	}

	public static abstract class CSVImportTask<E> extends ItemsLoadingTask<List<E>> {

		private final File file;

		public CSVImportTask(String itemsDesc, String itemDesc, BaseLoadingIndicator indicator, File file) {
			super(itemsDesc, itemDesc, indicator);

			this.file = file;
		}

		@Override
		protected List<E> call() throws Exception {
			Reader fr = null;
			CSVParser parser = null;

			try {
				fr = new FileReader(file);
				parser = new CSVParser(fr, FORMAT);

				int count = countLines(file);
				List<E> result = new ArrayList<>(count);

				loadingStarted(count);

				parser.forEach((record) -> {
					try {
						if (!this.isCancelled()) {
							loadingNextItem(null);
							E item = exctractItem(record);
							result.add(item);
						}
					} catch (Exception e) {
						DefrostException de = new DefrostException(itemDesc + " failed", e);
						throw new RuntimeException(de);
					}
				});

				postprocessItems(result);

				loadingFinished();
				return result;
			} catch (IOException e) {
				throw new DefrostException(itemsDesc + " failed", e);
			} finally {
				closeQuietly(fr);
				closeQuietly(parser);
			}
		}

		protected void postprocessItems(List<E> result) {
		}

		public abstract E exctractItem(CSVRecord record) throws ParseException;

	}

	public static abstract class CSVExportTask<E> extends ItemsLoadingTask<Void> {

		private final File file;
		private final List<E> items;

		public CSVExportTask(String itemsDesc, String itemDesc, BaseLoadingIndicator indicator, File file,
				List<E> items) {
			super(itemsDesc, itemDesc, indicator);

			this.file = file;
			this.items = items;
		}

		@Override
		protected Void call() throws Exception {
			Writer fw = null;
			CSVPrinter printer = null;
			try {
				fw = new FileWriter(file);
				printer = new CSVPrinter(fw, FORMAT);
				final CSVPrinter pr = printer;

				loadingStarted(items.size());

				preprocessItems(items);

				items.forEach((item) -> {
					if (!this.isCancelled()) {
						loadingNextItem(null);
						Object[] fields = exportItem(item);
						try {
							pr.printRecord(fields);
						} catch (Exception e) {
							DefrostException de = new DefrostException(itemDesc + " failed", e);
							throw new RuntimeException(de);
						}
					}
				});

				printer.flush();
				loadingFinished();
			} catch (IOException e) {
				throw new DefrostException(itemsDesc + " failed", e);
			} finally {
				closeQuietly(fw);
				closeQuietly(printer);
			}

			return null;
		}

		protected void preprocessItems(List<E> items) {
		}

		public abstract Object[] exportItem(E item);
	}

	private static int countLines(File file) throws IOException {
		// http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java

		LineNumberReader lnr = null;
		try {
			lnr = new LineNumberReader(new FileReader(file));

			lnr.skip(Long.MAX_VALUE);
			return lnr.getLineNumber() + 1;
		} catch (IOException e) {
			return Integer.MAX_VALUE;
		} finally {
			if (lnr != null) {
				lnr.close();
			}
		}
	}

	protected static void closeQuietly(Closeable closeable) throws DefrostException {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				throw new DefrostException("Cannot close file", e);
			}
		}
	}

}
