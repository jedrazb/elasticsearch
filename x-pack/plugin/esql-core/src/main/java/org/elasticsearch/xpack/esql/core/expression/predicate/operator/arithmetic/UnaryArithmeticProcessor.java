/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.esql.core.expression.predicate.operator.arithmetic;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xpack.esql.core.QlIllegalArgumentException;
import org.elasticsearch.xpack.esql.core.expression.gen.processor.Processor;

import java.io.IOException;
import java.util.function.Function;

public class UnaryArithmeticProcessor implements Processor {

    public enum UnaryArithmeticOperation {

        NEGATE(Arithmetics::negate);

        private final Function<Number, Number> process;

        UnaryArithmeticOperation(Function<Number, Number> process) {
            this.process = process;
        }

        public final Number apply(Number number) {
            return process.apply(number);
        }

        public String symbol() {
            return "-";
        }
    }

    public static final String NAME = "au";

    private final UnaryArithmeticOperation operation;

    public UnaryArithmeticProcessor(UnaryArithmeticOperation operation) {
        this.operation = operation;
    }

    public UnaryArithmeticProcessor(StreamInput in) throws IOException {
        operation = in.readEnum(UnaryArithmeticOperation.class);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(operation);
    }

    @Override
    public Object process(Object input) {
        if (input == null) {
            return null;
        }

        if (input instanceof Number number) {
            return operation.apply(number);
        }
        throw new QlIllegalArgumentException("A number is required; received {}", input);
    }

    @Override
    public String toString() {
        return operation.symbol() + super.toString();
    }
}
