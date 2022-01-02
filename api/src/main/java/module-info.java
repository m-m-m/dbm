/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides the API for mapping between database and {@link EntityBean}s.<br>
 */
@SuppressWarnings("all") //
module io.github.mmm.dbm {

  requires transitive io.github.mmm.bean.factory;

  requires transitive io.github.mmm.entity.bean;

  requires transitive reactor.core;

  requires transitive org.reactivestreams;

  uses io.github.mmm.dbm.DbClientFactory;

  exports io.github.mmm.dbm;

  exports io.github.mmm.dbm.config;

}
