package org.yakdanol.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yakdanol.homework.model.CustomLinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTests {

	private CustomLinkedList<String> customList;

	@BeforeEach
	public void setUp() {
		customList = new CustomLinkedList<>();
	}

	@Test
	public void testAdd() {
		customList.add("Element 1");
		customList.add("Element 2");
		customList.add("Element 3");

		assertEquals(3, customList.size());  // Проверяем, что размер списка 3
		assertEquals("Element 1", customList.get(0));  // Проверяем первый элемент
		assertEquals("Element 3", customList.get(2));  // Проверяем последний элемент
	}

	@Test
	public void testGet() {
		customList.add("Element 1");
		customList.add("Element 2");

		assertEquals("Element 1", customList.get(0));  // Проверяем первый элемент
		assertEquals("Element 2", customList.get(1));  // Проверяем второй элемент
	}

	@Test
	public void testRemove() {
		customList.add("Element 1");
		customList.add("Element 2");
		customList.add("Element 3");

		String removedElement = customList.remove(1);

		assertEquals("Element 2", removedElement);  // Проверяем, что удален второй элемент
		assertEquals(2, customList.size());  // Проверяем, что размер списка уменьшился до 2
		assertEquals("Element 3", customList.get(1));  // Проверяем, что элемент 3 теперь на позиции 1
	}

	@Test
	public void testContains() {
		customList.add("Element 1");
		customList.add("Element 2");

		assertTrue(customList.contains("Element 1"));  // Проверяем, что список содержит элемент 1
		assertFalse(customList.contains("Element 3"));  // Проверяем, что список не содержит элемент 3
	}

	@Test
	public void testAddAll() {
		List<String> elementsToAdd = new ArrayList<>();
		elementsToAdd.add("Element 4");
		elementsToAdd.add("Element 5");

		customList.add("Element 1");
		customList.add("Element 2");
		customList.addAll(elementsToAdd);

		assertEquals(4, customList.size());  // Проверяем, что размер списка увеличился до 4
		assertEquals("Element 4", customList.get(2));  // Проверяем, что новый элемент добавлен на правильную позицию
		assertEquals("Element 5", customList.get(3));  // Проверяем последний элемент
	}

	@Test
	public void testSize() {
		customList.add("Element 1");
		customList.add("Element 2");

		assertEquals(2, customList.size());  // Проверяем размер списка

		customList.remove(1);
		assertEquals(1, customList.size());  // Проверяем размер после удаления
	}

	@Test
	public void testStreamToCustomLinkedList() {
		Stream<String> stream = Stream.of("Element 6", "Element 7", "Element 8");
		CustomLinkedList<String> customListFromStream = stream.reduce(
				new CustomLinkedList<>(),
				(list, element) -> {
					list.add(element);
					return list;
				},
				(list1, list2) -> {
					list1.addAll((List<String>) list2);
					return list1;
				}
		);

		// Проверяем размер и содержимое списка
		assertEquals(3, customListFromStream.size());
		assertEquals("Element 6", customListFromStream.get(0));
		assertEquals("Element 7", customListFromStream.get(1));
		assertEquals("Element 8", customListFromStream.get(2));
	}
}
