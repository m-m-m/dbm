/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides the API for mapping between database and {@link EntityBean}s.<br>
 */
@SuppressWarnings("all") //
module io.github.mmm.dbm.api {

  requires transitive io.github.mmm.entity.dao;

  exports io.github.mmm.dbm.config;

}
