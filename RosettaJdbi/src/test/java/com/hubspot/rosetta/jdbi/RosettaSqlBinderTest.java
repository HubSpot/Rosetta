package com.hubspot.rosetta.jdbi;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.SQLStatement;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hubspot.rosetta.SnakeCase;
import com.hubspot.rosetta.StoredAsJson;

import static org.fest.assertions.api.Assertions.assertThat;

public class RosettaSqlBinderTest {

  private MockedRosettaSqlBinder binder;

  @Before
  public void setUp() {
    binder = new MockedRosettaSqlBinder();
  }

  @Test
  public void simple() {
    PersonDto person = new PersonDto();
    person.name = "Dennis";
    person.age = 37; // not old
    person.nickName = "Grand Slam";

    Multimap<String, Object> expected = ArrayListMultimap.create();
    expected.put("name", "Dennis");
    expected.put("nick_name", "Grand Slam");
    expected.put("id", "Dennis-37");
    expected.put("meta", "{}");
    // age isn't bound since Rosetta only looks at bean bindings that have getters

    binder.bindAll(null, person);

    assertThat(binder.bindings).isEqualTo(expected);
  }

  @Test
  public void storedAsJsonValue() {
    PersonDto person = new PersonDto();
    person.meta = new Person("Dennis");

    Multimap<String, Object> expected = ArrayListMultimap.create();
    expected.put("meta", "{\"name\": \"Dennis\"}");

    binder.bindAll(null, person);

    assertThat(binder.bindings).isLenientEqualsToByIgnoringFields(expected, "name", "nick_name", "id");
  }

  @Test
  public void guavaAndJoda() {
    Widget widget = new Widget();

    Multimap<String, Object> expected = ArrayListMultimap.create();
    expected.put("someDateTime", "42");
    expected.put("presentString", "foobar");
    expected.put("presentDouble", "1024.0");
    expected.put("absentLong", null);

    binder.bindAll(null, widget);

    assertThat(binder.bindings).isEqualTo(expected);
  }

  @Test
  public void binary() {
    BinaryThing binaryThing = new BinaryThing();
    String binaryData = "This is some binary data";
    binaryThing.setSomeBytes(binaryData.getBytes());
    binder.bindAll(null, binaryThing);

    // sadly we have to explicitly go with a byte[]
    // because the assertThat(binder.bindings).isEqualTo(expected); is not happy with byte array comparison
    assertThat(binder.bindings.size()).isEqualTo(1);
    assertThat(binder.bindings.get("someBytes")).isNotEmpty();
    byte[] val = (byte[]) binder.bindings.get("someBytes").iterator().next();
    assertThat(val).isEqualTo(binaryData.getBytes());
  }

  @SnakeCase
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

    // StoredAsJson
    @StoredAsJson(empty="{}") private Person meta;

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

  @SuppressWarnings("unused")
  private static class Widget {
    public DateTime getSomeDateTime() {
      return new DateTime(42);
    }

    public Optional<String> getPresentString() {
      return Optional.of("foobar");
    }

    public Optional<Double> getPresentDouble() {
      return Optional.of(1024d);
    }

    public Optional<Long> getAbsentLong() {
      return Optional.absent();
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

  private static class MockedRosettaSqlBinder extends RosettaSqlBinder {
    private final Multimap<String, Object> bindings;

    private MockedRosettaSqlBinder() {
      bindings = ArrayListMultimap.create();
    }

    @Override
    protected void bindOne(SQLStatement<?> q, String fieldName, Object value) {
      bindings.put(fieldName, value);
    }
  }
}
