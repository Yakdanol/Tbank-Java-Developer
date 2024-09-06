package org.yakdanol.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yakdanol.homework.model.CustomLinkedList;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeworkApplication.class, args);
		runCustomLinkedListTests();
	}

	public static void runCustomLinkedListTests() {
		CustomLinkedList<String> customList = new CustomLinkedList<>();

		// Тестирование метода add
		System.out.println("=== Тестирование метода add ===");
		customList.add("Element 1");
		customList.add("Element 2");
		customList.add("Element 3");
		System.out.println("After adding elements:");
		customList.printList(); // Element 1 Element 2 Element 3

		// Тестирование метода get
		System.out.println("\n=== Тестирование метода get ===");
		System.out.println("Element at index 1: " + customList.get(1)); // Element 2

		// Тестирование метода remove
		System.out.println("\n=== Тестирование метода remove ===");
		String removedElement = customList.remove(1);
		System.out.println("Removed element: " + removedElement); // Element 2
		System.out.println("After removing element at index 1:");
		customList.printList();  // Element 1 Element 3

		// Тестирование метода contains
		System.out.println("\n=== Тестирование метода contains ===");
		boolean containsElement3 = customList.contains("Element 3");
		boolean containsElement4 = customList.contains("Element 4");
		System.out.println("Contains 'Element 3': " + containsElement3); // true
		System.out.println("Contains 'Element 4': " + containsElement4); // false

		// Тестирование метода addAll
		System.out.println("\n=== Тестирование метода addAll ===");
		List<String> newElements = new ArrayList<>();
		newElements.add("Element 4");
		newElements.add("Element 5");
		customList.addAll(newElements);
		System.out.println("After adding all elements:");
		customList.printList();  // Element 1 Element 3 Element 4 Element 5

		// Тестирование метода size
		System.out.println("\n=== Тестирование метода size ===");
		System.out.println("Size of the list: " + customList.size()); // 4
	}
}
