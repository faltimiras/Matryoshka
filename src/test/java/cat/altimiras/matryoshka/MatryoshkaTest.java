package cat.altimiras.matryoshka;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MatryoshkaTest {

	@Test
	public void value() throws Exception {
		Matryoshka matryoshka = new Matryoshka(1);
		assertNull(matryoshka.get("/key/key2").value());
	}

	@Test
	public void simpleValue() throws Exception {

		Map<String, Object> content = new HashMap<>();
		content.put("value", "123456789");

		Matryoshka matryoshka = new Matryoshka(content);

		assertEquals("123456789", matryoshka.get("value").value());
		assertEquals("123456789", matryoshka.get("/value").value());
		assertEquals(1, matryoshka.get("value").asList().size());
		assertEquals("123456789", matryoshka.get("value").asList().get(0).value());
	}

	@Test
	public void object() throws Exception {

		Map<String, Object> obj = new HashMap<>();
		obj.put("value1", "111");
		obj.put("value2", "222");

		Map<String, Object> content = new HashMap<>();
		content.put("obj", obj);
		content.put("field1", "aaa");
		content.put("field2", "bbb");

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		assertEquals("111", matryoshka.get("root/obj/value1").value());
		assertEquals("222", matryoshka.get("/root/obj/value2").value());
		assertEquals("aaa", matryoshka.get("root/field1").value());
		assertEquals("bbb", matryoshka.get("root/field2").value());

		assertEquals("111", matryoshka.get("root/obj/value1").asList().get(0).value());
		assertEquals("222", matryoshka.get("/root").asList().get(0).get("/obj/value2").value());
		assertEquals("222", matryoshka.get("/root").asList().get(0).get("/obj/").asMatryoshka().get("value2").value());

		assertEquals("aaa", matryoshka.get("root").asMatryoshka().get("field1").value());
		assertEquals("bbb", matryoshka.get("root/field2").value());
	}

	@Test
	public void list() throws Exception {

		Map<String, Object> e1 = new HashMap<>(1);
		e1.put("key", "111");
		Map<String, Object> e2 = new HashMap<>(1);
		e2.put("key", "222");

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList(e1, e2));
		content.put("field1", "aaa");
		content.put("field2", "bbb");

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		assertEquals(2, matryoshka.get("root/list").asList().size());
		assertEquals("111", matryoshka.get("root/list").asList().get(0).get("key").value());
		assertEquals("222", matryoshka.get("root/list").asList().get(1).get("key").value());
		assertEquals("aaa", matryoshka.get("root/field1").value());
		assertEquals("bbb", matryoshka.get("root/field2").value());

		assertTrue(matryoshka.get("root/list").value() instanceof List);
	}

	@Test
	public void listPrimitives() throws Exception {

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList("1", 2, true));
		content.put("field1", "aaa");

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		assertEquals(3, matryoshka.get("root/list").asList().size());
		assertEquals("1", matryoshka.get("root/list").asList().get(0).value());
		assertEquals(2, matryoshka.get("root/list").asList().get(1).value());
		assertEquals(true, matryoshka.get("root/list").asList().get(2).value());

		assertEquals("aaa", matryoshka.get("root/field1").value());

		assertTrue(matryoshka.get("root/list").value() instanceof List);
	}

	@Test
	public void notExist() throws Exception {

		Map<String, Object> content = new HashMap<>();
		content.put("field1", "aaa");

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		assertNull(matryoshka.get("anotherroot").asMatryoshka());
		assertNull(matryoshka.get("anotherroot/otherfield").asMatryoshka());
		assertNull(matryoshka.get("anotherroot").value());
		assertNull(matryoshka.get("anotherroot/otherfield").value());
		assertTrue(matryoshka.get("anotherroot").asList().isEmpty());
		assertTrue(matryoshka.get("anotherroot/otherfield").asList().isEmpty());

	}

	@Test(expected = Exception.class)
	public void asMatryoshkaList() throws Exception {
		Map<String, Object> e1 = new HashMap<>(1);
		e1.put("key", "111");

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList(e1));

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		matryoshka.get("root/list").asMatryoshka();
	}

	@Test
	public void matryoshkaIterator() throws Exception {

		Map<String, Object> e1 = new HashMap<>(1);
		e1.put("key", "111");
		Map<String, Object> e2 = new HashMap<>(1);
		e2.put("key", "222");

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList(e1, e2));

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		Iterator<Matryoshka> iterator = matryoshka.get("root/list").asList().iterator();
		assertTrue(iterator.hasNext());
		assertEquals("111", iterator.next().get("key").value());
		assertTrue(iterator.hasNext());
		assertEquals("222", iterator.next().get("key").value());
		assertFalse(iterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void emptyIterator() throws Exception {

		Map<String, Object> content = new HashMap<>();
		content.put("list", new ArrayList<>());

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Matryoshka matryoshka = new Matryoshka(root);

		Iterator<Matryoshka> iterator = matryoshka.get("root/list").asList().iterator();
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());

		iterator.next();
	}
}