package com.hubspot.rosetta;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.hubspot.rosetta.beans.Blob;
import com.hubspot.rosetta.beans.Gender;
import com.hubspot.rosetta.beans.GenderPojo;
import com.hubspot.rosetta.beans.HorribleAbomination;
import com.hubspot.rosetta.beans.MaybePerson;
import com.hubspot.rosetta.beans.Parent;
import com.hubspot.rosetta.beans.Person;
import com.hubspot.rosetta.beans.PersonAlt;
import com.hubspot.rosetta.beans.Snake;
import com.hubspot.rosetta.mock.MockResultSet;
import com.hubspot.rosetta.mock.MockResultSetMetaData;

import static org.fest.assertions.api.Assertions.assertThat;

public class RosettaMapperTestCase {

  private ResultSet maybePersonResults;
  private ResultSet personResults;
  private ResultSet subPersonResults;
  private ResultSet personAltResults;
  private ResultSet parentResults;
  private ResultSet recursivePersonResults;
  private ResultSet horribleAbominationResults;
  private ResultSet snakeResults;
  private ResultSet blobResults;

  @Before
  public void setUp() throws SQLException {
    maybePersonResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{ "name", "age", "living"}),
                                              new Object[][]{ {"Bob", 15, true} });
    personResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{ "name", "age", "gender"}),
                                         new Object[][]{ {"Jill", 15, 1} });
    subPersonResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{ "name", "age", "gender", "subProperty"}),
                                         new Object[][]{ {"Jill", 15, 1, "foo"} });
    personAltResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{ "nom", "age", "gender"}),
                                            new Object[][]{ {"Jill", 15, 1} });
    parentResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{ "adultName", "child.age", "child.gender", "child.name"}),
      new Object[][]{ {"Bob's Dad", 15, 0, "Bob"} }
    );
    recursivePersonResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{ "name", "age", "gender", "child.age", "child.gender", "child.name"}),
                                                  new Object[][]{ {"Bob's Dad", 45, 0, 15, 0, "Bob"} }
                                         );

    horribleAbominationResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{"nom", "age", "gender", "foo"}),
        new Object[][]{{"Jill", 15, 1, "bar"}});
    snakeResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{"name", "has_stripes", "is_poisonous"}),
                                        new Object[][]{{"Adder", true, true}});
    blobResults = MockResultSet.create(MockResultSetMetaData.create(new String[]{"name", "blob", "anArray"}),
                                       new Object[][]{{"Blob", "{ \"name\": \"Blob2\"}", "[1, 2, 3]"}});

    maybePersonResults.next();
    personResults.next();
    subPersonResults.next();
    personAltResults.next();
    parentResults.next();
    horribleAbominationResults.next();
    recursivePersonResults.next();
    snakeResults.next();
    blobResults.next();
  }

  @Test
  public void simpleMap() {
    MaybePerson person = map(MaybePerson.class, maybePersonResults);

    assertThat(person.getName()).isEqualTo("Bob");
    assertThat(person.getAge()).isEqualTo(15);
    assertThat(person.isLiving()).isEqualTo(true);
  }

  @Test
  public void mapWithCustomValueEnum() {
    Person person = map(Person.class, personResults);

    assertThat(person.getName()).isEqualTo("Jill");
    assertThat(person.getAge()).isEqualTo(15);
    assertThat(person.getGender()).isEqualTo(Gender.FEMALE);
  }

  @Test
  public void mapWithCustomValuePojo() {
    PersonAlt person = map(PersonAlt.class, personAltResults);

    assertThat(person.getName()).isEqualTo("Jill");
    assertThat(person.getAge()).isEqualTo(15);
    // Can't test object equality due to Jackson cloning (?)
    assertThat(person.getGender().getDigit()).isEqualTo(GenderPojo.FEMALE.getDigit());
  }

  @Test
  public void mapWithInheritedProps() {
    HorribleAbomination person = map(HorribleAbomination.class, horribleAbominationResults);

    assertThat(person.getName()).isEqualTo("Jill");
    assertThat(person.getAge()).isEqualTo(15);
    assertThat(person.getGender().getDigit()).isEqualTo(GenderPojo.FEMALE.getDigit());
    assertThat(person.getFoo()).isEqualTo("bar");
  }

  @Test
  public void mapWithNested() {
    Parent person = map(Parent.class, parentResults);

    assertThat(person.getAdultName()).isEqualTo("Bob's Dad");
    assertThat(person.getChild().getName()).isEqualTo("Bob");
    assertThat(person.getChild().getAge()).isEqualTo(15);
    assertThat(person.getChild().getGender()).isEqualTo(Gender.MALE);
  }

  @Test
  public void mapWithSnakeCase() {
    Snake snake = map(Snake.class, snakeResults);

    assertThat(snake.getName()).isEqualTo("Adder");
    assertThat(snake.getHasStripes()).isTrue();
    assertThat(snake.getIsPoisonous()).isTrue();

  }

  @Test
  public void mapWithBlob() {
    Blob blob = map(Blob.class, blobResults);

    assertThat(blob.getName()).isEqualTo("Blob");
    assertThat(blob.getBlob().getName()).isEqualTo("Blob2");
    for (int i = 0; i < 3; i++) {
      assertThat(blob.getAnArray().get(i).isInt()).isTrue();
      assertThat(blob.getAnArray().get(i).asInt()).isEqualTo(i + 1);
    }
  }

  private <T> T map(Class<T> klass, ResultSet results) {
    try {
      return new RosettaMapper<T>(klass).mapRow(results);
    } catch(SQLException e) {}

    return null;
  }
}
