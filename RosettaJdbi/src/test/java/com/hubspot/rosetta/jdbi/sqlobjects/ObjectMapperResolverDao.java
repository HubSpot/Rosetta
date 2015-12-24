package com.hubspot.rosetta.jdbi.sqlobjects;

import com.hubspot.rosetta.jdbi.*;
import com.hubspot.rosetta.jdbi.beans.MapperNameBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

@RosettaResultSetObjectMapper("configSqlObjectResultSetMapper")
@RosettaStatementBinderObjectMapper("configSqlObjectBinderMapper")
@RegisterMapperFactory(RosettaMapperFactory.class)
public interface ObjectMapperResolverDao {

    @RosettaResultSetObjectMapper("configResultSetMapper")
    @RosettaStatementBinderObjectMapper("configBinderMapper")
    @SqlQuery("SELECT 'value' \"mapper_name\", :binder_name \"binder_name\"")
    MapperNameBean configMapperAndBinderWithArgBinder(
            @RosettaStatementBinderObjectMapper("bwrConfigBinderMapper") @BindWithRosetta MapperNameBean mnb);

    @RosettaResultSetObjectMapper("configResultSetMapper")
    @RosettaStatementBinderObjectMapper("configBinderMapper")
    @SqlQuery("SELECT 'value' \"mapper_name\", :binder_name \"binder_name\"")
    MapperNameBean configMapperAndBinder(@BindWithRosetta MapperNameBean mnb);

    @RosettaObjectMapper("configDefaultMapper")
    @SqlQuery("SELECT 'value' \"mapper_name\", :binder_name \"binder_name\"")
    MapperNameBean configDefaultMapper(@BindWithRosetta MapperNameBean mnb);


    @SqlQuery("SELECT 'value' \"mapper_name\", :binder_name \"binder_name\"")
    MapperNameBean sqlObjectMappers(@BindWithRosetta MapperNameBean mnb);

    @RosettaResultSetObjectMapper("configResultSetMapper")
    @SqlQuery("SELECT 'value' \"mapper_name\", :binder_name \"binder_name\"")
    MapperNameBean configResultSetMapperAndSqlObjectBinder(@BindWithRosetta MapperNameBean mnb);

}
