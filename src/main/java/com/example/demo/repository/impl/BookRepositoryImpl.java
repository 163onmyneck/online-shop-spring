package com.example.demo.repository.impl;

import com.example.demo.exception.DataProcessionException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessionException("Can not save book to DB. Book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book b", Book.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessionException("Can not get all books from DB.", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery("FROM Book WHERE id = :id",
                            Book.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (Exception e) {
            throw new DataProcessionException("Can not get book by id: " + id, e);
        }
    }
}