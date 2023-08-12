package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

  private final ConcurrentHashMap<Long, String> data = new ConcurrentHashMap<>();
  private AtomicLong counter = new AtomicLong(0);
  public List<Post> all() {
    if (!data.isEmpty()) {
      List<Post> list = new ArrayList<>();
      for (Map.Entry<Long, String> entry : data.entrySet()) {
        Post post = new Post(entry.getKey(), entry.getValue());
        list.add(post);
      }
      return list;
    }
    return Collections.emptyList();
  }

  public Optional<Post> getById(long id) {
    if (data.containsKey(id)) {
      return Optional.of(new Post(id, data.get(id)));
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    var id = post.getId();
    var content = post.getContent();
    try {
      if (id == 0) {
        counter.getAndIncrement();
        data.put(counter.get(), content);
      }
      if (id != 0 && data.containsKey(id)) {
        data.put(id, content);
      }
    } catch (NotFoundException e) {
      throw new NotFoundException("The field with id=" + id + " does not exist", e);
    }

    return post;
  }

  public void removeById(long id) {
    try {
      if (data.containsKey(id)) {
        data.remove(id);
      }
    } catch (NotFoundException e) {
      throw new NotFoundException("The field with id=" + id + " does not exist", e);
    }
  }
}
