package cz.martlin.defrost.tasks;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.martlin.defrost.test.ConsoleLoadingIndicator;

public class ItemsLoadingTaskTest {
	private static final Logger LOG = LoggerFactory.getLogger(ItemsLoadingTaskTest.class);

	@BeforeClass
	public static void check() {
		try {
			ConsoleLoadingIndicator.initToolkit();
		} catch (ExceptionInInitializerError e) {
			LOG.warn("Cannot initialize FX toolkit (are you in console?), test skipped", e);
			Assume.assumeNoException(e);
		}

	}

	@Test
	public void test() throws InterruptedException, ExecutionException, TimeoutException {
		BaseLoadingIndicator ind = new ConsoleLoadingIndicator(System.out);
		ItemsLoadingTask<String> task = new ChaptersCountingTask(ind);

		task.run();

		String result = task.get(
				2 //
						* ChaptersCountingTask.TIMEOUT_IN_MS //
						* ChaptersCountingTask.CHAPTERS_COUNT //
						* ChaptersCountingTask.SUBCHAPTERS_COUNT, //
				TimeUnit.MILLISECONDS);

		assertEquals(ChaptersCountingTask.RESULT, result);

	}

	public static class ChaptersCountingTask extends ItemsLoadingTask<String> {

		private static final String RESULT = "done or whatever";
		protected static final int CHAPTERS_COUNT = 10;
		protected static final int SUBCHAPTERS_COUNT = 5;
		protected static final int TIMEOUT_IN_MS = 1;

		public ChaptersCountingTask(BaseLoadingIndicator indicator) {
			super("Reading chapters", "Reading chapter", indicator);
		}

		@Override
		protected String call() throws Exception {
			loadingStarted(CHAPTERS_COUNT);

			for (int i = 0; i < CHAPTERS_COUNT; i++) {
				loadingNextItem(Integer.toString(i));
				for (int j = 0; j < SUBCHAPTERS_COUNT; j++) {
					loadingItemUpdated("subchapter " + Integer.toString(j));
					Thread.sleep(TIMEOUT_IN_MS);
				}
			}

			loadingFinished();

			return RESULT;
		}

	}

}
