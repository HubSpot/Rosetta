package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.RosettaBinder.Callback;
import com.hubspot.rosetta.annotations.RosettaNaming;
import com.hubspot.rosetta.annotations.StoredAsJson;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class RosettaJdbiBinderTest {
  private MockCallback callback;

  @Before
  public void setUp() {
    callback = new MockCallback();
  }

  @Test
  public void simple() {
    PersonDto person = new PersonDto();
    person.name = "Dennis";
    person.age = 37; // not old
    person.nickName = "Grand Slam";

    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put("name", "Dennis");
    expected.put("nick_name", "Grand Slam");
    expected.put("id", "Dennis-37");
    expected.put("meta", "{}");
    // age isn't bound since Rosetta only looks at bean bindings that have getters

    assertThat(bind(person)).isEqualTo(expected);
  }

  @Test
  public void storedAsJsonValue() {
    PersonDto person = new PersonDto();
    person.meta = new Person("Dennis");

    assertThat(bind(person).get("meta")).isEqualTo("{\"name\":\"Dennis\"}");
  }

  @Test
  public void binary() {
    BinaryThing binaryThing = new BinaryThing();
    String binaryData = "This is some binary data";
    binaryThing.setSomeBytes(binaryData.getBytes());

    assertThat(bind(binaryThing).get("someBytes")).isEqualTo(binaryData.getBytes());
  }

  private Map<String, Object> bind(Object o) {
    return bindWithPrefix("", o);
  }

  private Map<String, Object> bindWithPrefix(String prefix, Object o) {
    JsonNode node = Rosetta.getMapper().valueToTree(o);
    RosettaBinder.INSTANCE.bind(prefix, node, callback);
    return callback.getBindings();
  }

  @RosettaNaming(value = LowerCaseWithUnderscoresStrategy.class)
  public static class PersonDto {
    // field and getter
    private String name;

    public String getName() {
      return name;
    }

    // field, no getter
    private long age;

    // getter, no field
    public String getId() {
      return createId(name, age);
    }

    // snakes!
    private String nickName;

    public String getNickName() {
      return nickName;
    }

    public void setNickName(String nickName) {
      this.nickName = nickName;
    }

    @StoredAsJson(empty = "{}")
    private Person meta;

    public Person getMeta() {
      return meta;
    }

    public void setMeta(Person meta) {
      this.meta = meta;
    }

    static String createId(String name, long dob) {
      return String.format("%s-%d", name, dob);
    }
  }

  private static class Person {
    private String name;

    public Person(String name) {
      this.name = name;
    }

    @SuppressWarnings("unused")
    public String getName() {
      return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
      this.name = name;
    }
  }

  private static class BinaryThing {
    byte[] someBytes;
    public byte[] getSomeBytes() {
      return someBytes;
    }

    public void setSomeBytes(byte[] someBytes) {
      this.someBytes = someBytes;
    }
  }

  private static class MockCallback implements Callback {
    private final Map<String, Object> bindings = new HashMap<String, Object>();

    @Override
    public void bind(String key, Object value) {
      bindings.put(key, value);
    }

    public Map<String, Object> getBindings() {
      return bindings;
    }
  }
}
