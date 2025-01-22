package com.alura.literalura;

import com.alura.literalura.entity.Libro;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.GoogleBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private GoogleBooksService googleBooksService;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("=== Menú ===");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Listar libros por idioma");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1 -> buscarLibro(scanner);
                case 2 -> listarLibros();
                case 3 -> listarLibrosPorIdioma(scanner);
                case 4 -> continuar = false;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void buscarLibro(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();

        if (libroRepository.existsByTitulo(titulo)) {
            System.out.println("El libro ya está registrado en la base de datos.");
            return;
        }

        Libro libro = googleBooksService.buscarLibroPorTitulo(titulo);
        if (libro != null) {
            libroRepository.save(libro);
            System.out.println("Libro registrado: " + libro);
        } else {
            System.out.println("No se encontró el libro.");
        }
    }

    private void listarLibros() {
        var libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma(Scanner scanner) {
        System.out.print("Ingrese el idioma (ES, EN, FR, PT): ");
        String idioma = scanner.nextLine();

        var libros = libroRepository.findByIdioma(idioma);
        libros.forEach(System.out::println);
    }
}
