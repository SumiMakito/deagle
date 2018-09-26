package com.oasisfeng.hack;

import com.oasisfeng.hack.Hack.Unchecked;

import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;

import static com.oasisfeng.hack.Hack.ANY_TYPE;
import static com.oasisfeng.hack.Hack.onlyIf;

/**
 * Test cases for {@link Hack}
 *
 * Created by Oasis on 2015/8/24.
 */
public class HackTest extends TestCase {

	public void testBasicMethodAndConstructor() throws IOException {
		final Hack.HackedMethod1<Simple, Void, IOException, Unchecked, Unchecked, Integer> constructor
				= Hack.into(Simple.class).constructor().throwing(IOException.class).withParam(int.class);
		assertNotNull(constructor);;
		final Simple simple = constructor.invoke(0).statically();
		assertNotNull(simple);

		final Hack.HackedMethod0<Integer, Simple, Unchecked, Unchecked, Unchecked> foo
				= Hack.into(Simple.class).method("foo").returning(int.class).withoutParams();
		assertNotNull(foo);
		assertEquals(7, (int) foo.invoke().on(simple));

		final Hack.HackedMethod0<Integer, Simple, RuntimeException, Unchecked, Unchecked> foo_rt_ex
				= Hack.into(Simple.class).method("foo").returning(int.class).throwing(RuntimeException.class).withoutParams();
		assertNotNull(foo_rt_ex);
		assertEquals(7, (int) foo_rt_ex.invoke().on(simple));

		final Hack.HackedMethod0<Integer, Simple, IOException, Unchecked, Unchecked> foo_ex
				= Hack.into(Simple.class).method("foo").returning(int.class).throwing(IOException.class).withoutParams();
		assertNotNull(foo_ex);
		assertEquals(7, (int) foo_ex.invoke().on(simple));

		final Hack.HackedMethod3<Void, Void, IOException, Unchecked, Unchecked, Integer, String, Simple> bar
				= Hack.into(Simple.class).staticMethod("bar").throwing(IOException.class).withParams(int.class, String.class, Simple.class);
		assertNotNull(bar);
		bar.invoke(-1, "xyz", simple).statically();

		assertFail(null, Hack.into(Simple.class).method("bar").throwing(UnsupportedOperationException.class, FileNotFoundException.class).withParams(int.class, String.class, Simple.class));

		assertFail(NoSuchMethodException.class, Hack.into(Simple.class).method("notExist").withoutParams());
		assertFail(NoSuchMethodException.class, Hack.into(Simple.class).method("foo").withParam(int.class));
		assertFail(null, Hack.into(Simple.class).staticMethod("foo").withoutParams());
		assertFail(null, Hack.into(Simple.class).method("foo").returning(Void.class).withoutParams());
	}

	public void testMethodReturningAnyType() throws IOException {
		final Hack.HackedMethod0<?, Simple, Unchecked, Unchecked, Unchecked> foo
				= Hack.into(Simple.class).method("foo").returning(ANY_TYPE).withoutParams();
		assertNotNull(foo);
		assertEquals(7, foo.invoke().on(new Simple(0)));
	}

	public void testMethodFallback() throws IOException {
		final Hack.HackedMethod1<Integer, Simple, IOException, Unchecked, Unchecked, Void> foo_wrong
				= Hack.into(Simple.class).method("foo").throwing(IOException.class).returning(int.class).fallbackReturning(-1).withParam(Void.class);
		assertTrue(foo_wrong.isAbsent());		// Checked exceptions mismatch
		assertEquals(-1, (int) foo_wrong.invoke().on(new Simple(0)));

		final Hack.HackedMethod1<Integer, Simple, Unchecked, Unchecked, Unchecked, Void> foo_absent
				= Hack.into(Simple.class).method("foo").returning(int.class).fallbackReturning(-1).withParam(Void.class);
		assertTrue(foo_absent.isAbsent());
		assertEquals(-1, (int) foo_absent.invoke().on(new Simple(0)));
	}

	public void testConstructorFallback() throws IOException {
		final Hack.HackedMethod1<Simple, Void, Unchecked, Unchecked, Unchecked, Integer> constructor
				= Hack.into(Simple.class).constructor().withParam(int.class);
		assertTrue(constructor.isAbsent());

		final Hack.HackedMethod0<Simple, Void, Unchecked, Unchecked, Unchecked> absent_constructor
				= Hack.into(Simple.class).constructor().withoutParams();
		assertTrue(absent_constructor.isAbsent());
	}

	public void testBasicField() throws IOException {
		final Simple simple = new Simple(0);
		final Hack.HackedField<Simple, Integer> field = Hack.into(Simple.class).field("mIntField").ofType(int.class);
		assertNotNull(field);
		field.set(simple, 3);
		assertEquals(3, (int) field.get(simple));

		assertFail(null, Hack.into(Simple.class).staticField("mIntField").ofType(ANY_TYPE));
	}

	public void testFieldFallback() {
		final Hack.HackedTargetField<Integer> field = Hack.into(Simple.class).staticField("mIntField").fallbackTo(-1);
		assertEquals(-1, (int) field.get());
		field.set(0);
		assertEquals(-1, (int) field.get());
	}

	public void testClassNotFound() {
		assertFail(ClassNotFoundException.class, Hack.into("NoSuchClass").method("nonSense").withoutParams());
		assertFail(ClassNotFoundException.class, Hack.into(Simple.class).field("mIntField").ofType("NoSuchType"));
		assertFail(ClassNotFoundException.class, Hack.into(Simple.class).staticField("mStaticField").ofType("NoSuchType"));
	}

	public void testOnlyIf() throws IOException {
		final Hack.HackedMethod1<Simple, Void, IOException, Unchecked, Unchecked, Integer> constructor
				= onlyIf(true).into(Simple.class).constructor().throwing(IOException.class).withParam(int.class);
		assertNotNull(constructor);
		final Simple simple = constructor.invoke(0).statically();
		assertNotNull(simple);

		final Hack.HackedMethod1<Simple, Void, IOException, Unchecked, Unchecked, Integer> fallback_constructor
				= Hack.onlyIf(false).into(Simple.class).constructor().throwing(IOException.class).withParam(int.class);
		assertNotNull(fallback_constructor);
		final Simple value = fallback_constructor.invoke(0).statically();
		assertNull(value);

		final Hack.HackedMethod1<Integer, Simple, IOException, Unchecked, Unchecked, Void> foo_wrong
				= Hack.into(Simple.class).method("foo").throwing(IOException.class).returning(int.class).withParam(Void.class);
		assertNull(foo_wrong);

		final Hack.HackedField<Simple, Integer> field = Hack.onlyIf(true).into(Simple.class).field("mIntField").ofType(int.class);
		assertNotNull(field);
		field.set(simple, 3);
		assertEquals(3, (int) field.get(simple));

		final Hack.HackedField<Simple, Integer> absent_field = Hack.onlyIf(false).into(Simple.class).field("mIntField").ofType(int.class);
		assertNull(absent_field);

		final Hack.HackedField<Simple, Integer> fallback_field = Hack.onlyIf(false).into(Simple.class).field("mIntField").fallbackTo(-1);
		assertNotNull(fallback_field);
		fallback_field.set(simple, 3);
		assertEquals(-1, (int) fallback_field.get(simple));
	}

	@Override protected void setUp() throws Exception {
		super.setUp();
		Hack.setAssertionFailureHandler(new Hack.AssertionFailureHandler() { @Override public void onAssertionFailure(@NonNull final Hack.AssertionException failure) {
			mFailure = failure;
		}});
		mFailure = new Hack.AssertionException(new IOException());
		assertFail(IOException.class, null);
		mFailure = null;
	}

	private void assertFail(Class<? extends Throwable> failure, final Object hack) {
		assertNull(hack);
		assertNotNull(mFailure);
		if (failure != null) {
			assertNotNull(mFailure.getCause());
			assertEquals(failure, mFailure.getCause().getClass());
		}
		mFailure = null;
	}

	private Hack.AssertionException mFailure;

	@SuppressWarnings("unused") private static class Simple {

		Simple(final int x) throws IOException {}
		int foo() { return 7; }
		private static void bar(int type, String name, Simple simple) throws IOException {}
		int mIntField;
	}
}
