//package com.example.Todo.repo;
//import com.example.Todo.model.todo;
//
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicLong;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public class InMemRepo {
//    private  final Map<Long, todo> store = new ConcurrentHashMap<>();
//    private final AtomicLong idGenerator = new AtomicLong(1);
//    public List<todo> findAll() {
//        return new ArrayList<>(store.values());
//    }
//    public Optional<todo> findById(Long id) {
//        return Optional.ofNullable(store.get(id));
//    }
//    public todo save(todo t) {
//        if (t.getId() == null) {
//            Long id = idGenerator.getAndIncrement();
//            t.setId(id);
//            t.setCreatedAt(java.time.Instant.now());
//
//        }
//        t.setUpdatedAt(java.time.Instant.now());
//        store.put(t.getId(), t);
//        return t;
//    }
//    public void deleteById(Long id) {
//        store.remove(id);
//    }
//    public  void deleteAll() {
//        store.clear();
//    }
//
//}
