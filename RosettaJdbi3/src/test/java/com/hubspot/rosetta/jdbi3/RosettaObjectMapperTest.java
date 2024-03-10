package com.hubspot.rosetta.jdbi3;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.SqlStatement;
import org.junit.Test;

public class RosettaObjectMapperTest extends AbstractJdbiTest {

  private static final int JDBI_ADD = 1;
  private static final int HANDLE_ADD = 2;
  private static final int STATEMENT_ADD = 3;

  @Test
  public void bindingUsesJdbiOverride() {
    registerSerializer(getJdbi());

    TestObject inserted = new TestObject(1, "test");
    getDao().insert(inserted);

    TestObject expected = new TestObject(
      inserted.getId() + JDBI_ADD,
      inserted.getName() + JDBI_ADD
    );
    List<TestObject> results = getDao().getAll();
    assertThat(results).hasSize(1);

    TestObject actual = results.get(0);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void mappingUsesJdbiOverride() {
    registerDeserializer(getJdbi());

    TestObject inserted = new TestObject(1, "test");
    getDao().insert(inserted);

    TestObject expected = new TestObject(
      inserted.getId() + JDBI_ADD,
      inserted.getName() + JDBI_ADD
    );
    List<TestObject> results = getDao().getAll();
    assertThat(results).hasSize(1);

    TestObject actual = results.get(0);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void bindingUsesHandleOverride() {
    registerSerializer(getJdbi());

    TestObject inserted = new TestObject(1, "test");
    TestObject expected = new TestObject(
      inserted.getId() + HANDLE_ADD,
      inserted.getName() + HANDLE_ADD
    );

    TestObject actual = getDao()
      .withHandle(handle -> {
        registerSerializer(handle);

        TestDao dao = handle.attach(TestDao.class);
        dao.insert(inserted);

        List<TestObject> results = dao.getAll();
        assertThat(results).hasSize(1);

        return results.get(0);
      });

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void mappingUsesHandleOverride() {
    registerSerializer(getJdbi());

    TestObject inserted = new TestObject(1, "test");
    TestObject expected = new TestObject(
      inserted.getId() + HANDLE_ADD,
      inserted.getName() + HANDLE_ADD
    );

    TestObject actual = getDao()
      .withHandle(handle -> {
        registerDeserializer(handle);

        TestDao dao = handle.attach(TestDao.class);
        dao.insert(inserted);

        List<TestObject> results = dao.getAll();
        assertThat(results).hasSize(1);

        return results.get(0);
      });

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void mappingUsesStatementOverride() {
    registerSerializer(getJdbi());

    TestObject inserted = new TestObject(1, "test");
    TestObject expected = new TestObject(
      inserted.getId() + STATEMENT_ADD,
      inserted.getName() + STATEMENT_ADD
    );

    TestObject actual = getDao()
      .withHandle(handle -> {
        registerDeserializer(handle);

        TestDao dao = handle.attach(TestDao.class);
        dao.insert(inserted);

        Query query = handle.createQuery("SELECT * FROM test_table");
        registerDeserializer(query);

        List<TestObject> results = query.mapTo(TestObject.class).list();
        assertThat(results).hasSize(1);

        return results.get(0);
      });

    assertThat(actual).isEqualTo(expected);
  }

  private static void registerSerializer(Jdbi jdbi) {
    registerSerializer(jdbi.getConfig(), new TestObjectJsonSerializer(JDBI_ADD));
  }

  private static void registerDeserializer(Jdbi jdbi) {
    registerDeserializer(jdbi.getConfig(), new TestObjectJsonDeserializer(JDBI_ADD));
  }

  private static void registerSerializer(Handle handle) {
    registerSerializer(handle.getConfig(), new TestObjectJsonSerializer(HANDLE_ADD));
  }

  private static void registerDeserializer(Handle handle) {
    registerDeserializer(handle.getConfig(), new TestObjectJsonDeserializer(HANDLE_ADD));
  }

  private static void registerDeserializer(SqlStatement<?> stmt) {
    registerDeserializer(stmt.getConfig(), new TestObjectJsonDeserializer(STATEMENT_ADD));
  }

  private static void registerSerializer(
    ConfigRegistry config,
    JsonSerializer<TestObject> serializer
  ) {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.setSerializers(new SimpleSerializers(Collections.singletonList(serializer)));
    objectMapper.registerModule(module);

    config.get(RosettaObjectMapper.class).setObjectMapper(objectMapper);
  }

  private static void registerDeserializer(
    ConfigRegistry config,
    JsonDeserializer<TestObject> deserializer
  ) {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.setDeserializers(
      new SimpleDeserializers(Collections.singletonMap(TestObject.class, deserializer))
    );
    objectMapper.registerModule(module);

    config.get(RosettaObjectMapper.class).setObjectMapper(objectMapper);
  }

  private static class TestObjectJsonSerializer extends StdSerializer<TestObject> {

    private final int add;

    public TestObjectJsonSerializer(int add) {
      super(TestObject.class);
      this.add = add;
    }

    @Override
    public void serialize(
      TestObject value,
      JsonGenerator gen,
      SerializerProvider provider
    ) throws IOException {
      gen.writeStartObject();
      gen.writeFieldName("id");
      gen.writeNumber(value.getId() + add);
      gen.writeFieldName("name");
      gen.writeString(value.getName() + add);
      gen.writeEndObject();
    }
  }

  private static class TestObjectJsonDeserializer extends StdDeserializer<TestObject> {

    private final int add;

    public TestObjectJsonDeserializer(int add) {
      super(TestObject.class);
      this.add = add;
    }

    @Override
    public TestObject deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
      ObjectNode node = p.readValueAsTree();

      int id = node.get("id").intValue() + add;
      String name = node.get("name").textValue() + add;

      return new TestObject(id, name);
    }
  }
}
