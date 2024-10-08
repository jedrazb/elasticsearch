/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the "Elastic License
 * 2.0", the "GNU Affero General Public License v3.0 only", and the "Server Side
 * Public License v 1"; you may not use this file except in compliance with, at
 * your election, the "Elastic License 2.0", the "GNU Affero General Public
 * License v3.0 only", or the "Server Side Public License, v 1".
 */

package org.elasticsearch.nativeaccess.exports;

import org.elasticsearch.jdk.ModuleQualifiedExportsService;

public class NativeAccessModuleExportsService extends ModuleQualifiedExportsService {
    @Override
    protected void addExports(String pkg, Module target) {
        module.addExports(pkg, target);
    }

    @Override
    protected void addOpens(String pkg, Module target) {
        module.addOpens(pkg, target);
    }
}
