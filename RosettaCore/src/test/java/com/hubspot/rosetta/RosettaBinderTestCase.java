package com.hubspot.rosetta;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.hubspot.rosetta.beans.Gender;
import com.hubspot.rosetta.beans.Parent;
import com.hubspot.rosetta.beans.Person;
import com.hubspot.rosetta.beans.PersonAlt;
import com.hubspot.rosetta.beans.Snake;

public class RosettaBinderTestCase {

  @Test
  public void makeBoundMapOfParent() {
    final RosettaBinder binder = new RosettaBinder(Parent.class);

    final Person child = new Person();
    child.setAge(10);
    child.setName("Bob");
    child.setGender(Gender.MALE);

    final Parent parent = new Parent();
    parent.setAdultName("Jill");
    parent.setChild(child);

    final Map<String, Object> map = binder.makeBoundMap(parent);

    assertThat(map.size()).isEqualTo(4);

    assertThat(map.get("adultName")).isEqualTo("Jill");
    assertThat(map.get("child.name")).isEqualTo("Bob");
    assertThat(map.get("child.age")).isEqualTo(10);
    assertThat(map.get("child.gender")).isEqualTo(Gender.MALE);
  }

  @Test
  public void bindWithSnakeCase() {
    final RosettaBinder binder = new RosettaBinder(Snake.class);

    final Snake snake = new Snake();
    snake.setName("Adder");
    snake.setHasStripes(true);
    snake.setIsPoisonous(true);

    final Map<String, Object> map = binder.makeBoundMap(snake);

    assertThat(map.containsKey("has_stripes")).isTrue();
    assertThat(map.containsKey("is_poisonous")).isTrue();
  }

  @Test
  public void bindWithColumnName() {
    final RosettaBinder binder = new RosettaBinder(PersonAlt.class);

    final PersonAlt person = new PersonAlt();
    person.setName("Jill");

    final Map<String, Object> map = binder.makeBoundMap(person);

    assertThat(map.get("nom")).isEqualTo("Jill");
  }

}
