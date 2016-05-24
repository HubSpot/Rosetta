package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.jdbi.beans.MapperNameBean;
import com.hubspot.rosetta.jdbi.jackson.ObjectMapperResolutionTestModule;
import com.hubspot.rosetta.jdbi.sqlobjects.ObjectMapperResolverDao;
import org.junit.*;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import static org.assertj.core.api.Assertions.assertThat;


public class ObjectMapperResolutionTest {

    private DBI dbi;
    private ObjectMapper dbiDefaultMapper;
    private ObjectMapper dbiResultSetMapper;
    private ObjectMapper dbiBinderMapper;
    private ObjectMapper rosettaConfigurationDefaultMapper;
    private ObjectMapper rosettaConfigurationResultSetMapper;
    private ObjectMapper rosettaConfigurationBinderMapper;
    private ObjectMapper bindWithRosettaConfigurationBinderMapper;
    private ObjectMapper sqlObjectResultSetMapper;
    private ObjectMapper sqlObjectBinderMapper;


    @Before
    public void setUp() {

        dbi = new DBI("jdbc:h2:~/test;MODE=PostgreSQL");

        Rosetta.setMapper(getTaggedObjectMapper("globalMapper"));

        dbiDefaultMapper = getTaggedObjectMapper("dbiDefaultMapper");
        dbiResultSetMapper = getTaggedObjectMapper("dbiResultSetMapper");
        dbiBinderMapper = getTaggedObjectMapper("dbiBinderMapper");

        rosettaConfigurationDefaultMapper = getTaggedObjectMapper("configDefaultMapper");
        rosettaConfigurationResultSetMapper = getTaggedObjectMapper("configResultSetMapper");
        rosettaConfigurationBinderMapper = getTaggedObjectMapper("configBinderMapper");
        sqlObjectResultSetMapper = getTaggedObjectMapper("configSqlObjectResultSetMapper");
        sqlObjectBinderMapper = getTaggedObjectMapper("configSqlObjectBinderMapper");


        Rosetta.registerNamedMapper("configDefaultMapper",rosettaConfigurationDefaultMapper);
        Rosetta.registerNamedMapper("configResultSetMapper",rosettaConfigurationResultSetMapper);
        Rosetta.registerNamedMapper("configBinderMapper",rosettaConfigurationBinderMapper);
        Rosetta.registerNamedMapper("configSqlObjectResultSetMapper",sqlObjectResultSetMapper);
        Rosetta.registerNamedMapper("configSqlObjectBinderMapper",sqlObjectBinderMapper);


        bindWithRosettaConfigurationBinderMapper = getTaggedObjectMapper("bwrConfigBinderMapper");
        Rosetta.registerNamedMapper("bwrConfigBinderMapper", bindWithRosettaConfigurationBinderMapper);

    }

    @Test
    public void itResolvesObjectMappersAcrossDBIHierarchyCorrectly() {

        new RosettaObjectMapperOverride(dbiResultSetMapper).overrideResultSetMapper(dbi);
        new RosettaObjectMapperOverride(dbiBinderMapper).overrideStatementBinder(dbi);

        MapperNameBean mnb;

        try (Handle h = dbi.open()) {

            ObjectMapperResolverDao dao = h.attach(ObjectMapperResolverDao.class);

            mnb = dao.configMapperAndBinderWithArgBinder(new MapperNameBean());

            assertThat(mnb.getBinderName().getName()).isEqualTo("bwrConfigBinderMapper");
            assertThat(mnb.getMapperName().getName()).isEqualTo("configResultSetMapper");

            mnb = dao.configMapperAndBinder(new MapperNameBean());

            assertThat(mnb.getBinderName().getName()).isEqualTo("configBinderMapper");
            assertThat(mnb.getMapperName().getName()).isEqualTo("configResultSetMapper");

            mnb = dao.configDefaultMapper(new MapperNameBean());

            assertThat(mnb.getBinderName().getName()).isEqualTo("configDefaultMapper");
            assertThat(mnb.getMapperName().getName()).isEqualTo("configDefaultMapper");

            mnb = dao.sqlObjectMappers(new MapperNameBean());

            assertThat(mnb.getBinderName().getName()).isEqualTo("configSqlObjectBinderMapper");
            assertThat(mnb.getMapperName().getName()).isEqualTo("configSqlObjectResultSetMapper");

            mnb = dao.configResultSetMapperAndSqlObjectBinder(new MapperNameBean());

            assertThat(mnb.getBinderName().getName()).isEqualTo("configSqlObjectBinderMapper");
            assertThat(mnb.getMapperName().getName()).isEqualTo("configResultSetMapper");

            Query<?> q =  h.createQuery("SELECT 'value' \"mapper_name\", :binder_name \"binder_name\"");
            q.registerMapper(new RosettaMapperFactory());
            RosettaJdbiBinder.INSTANCE.bind(q, new MapperNameBean());
            mnb = q.mapTo(MapperNameBean.class).first();

            assertThat(mnb.getBinderName().getName()).isEqualTo("dbiBinderMapper");
            assertThat(mnb.getMapperName().getName()).isEqualTo("dbiResultSetMapper");

        }
    }

    @After
    public void tearDown() {

        Rosetta.clearNamedMappers();
        Rosetta.setMapper(new ObjectMapper());
    }

    private ObjectMapper getTaggedObjectMapper(final String name) {
       return  new ObjectMapper().registerModule(new ObjectMapperResolutionTestModule(name));
    }
}
