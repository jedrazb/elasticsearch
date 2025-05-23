/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.expression.function.scalar.convert;

import com.carrotsearch.randomizedtesting.annotations.Name;
import com.carrotsearch.randomizedtesting.annotations.ParametersFactory;

import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.time.DateUtils;
import org.elasticsearch.index.mapper.DateFieldMapper;
import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.xpack.esql.core.expression.Expression;
import org.elasticsearch.xpack.esql.core.tree.Source;
import org.elasticsearch.xpack.esql.core.type.DataType;
import org.elasticsearch.xpack.esql.expression.function.AbstractScalarFunctionTestCase;
import org.elasticsearch.xpack.esql.expression.function.TestCaseSupplier;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.elasticsearch.xpack.esql.core.util.SpatialCoordinateTypes.CARTESIAN;
import static org.elasticsearch.xpack.esql.core.util.SpatialCoordinateTypes.GEO;

public class ToStringTests extends AbstractScalarFunctionTestCase {
    public ToStringTests(@Name("TestCase") Supplier<TestCaseSupplier.TestCase> testCaseSupplier) {
        this.testCase = testCaseSupplier.get();
    }

    @ParametersFactory
    public static Iterable<Object[]> parameters() {
        // TODO multivalue fields
        String read = "Attribute[channel=0]";
        List<TestCaseSupplier> suppliers = new ArrayList<>();
        TestCaseSupplier.forUnaryInt(
            suppliers,
            "ToStringFromIntEvaluator[integer=" + read + "]",
            DataType.KEYWORD,
            i -> new BytesRef(Integer.toString(i)),
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            List.of()
        );
        TestCaseSupplier.forUnaryLong(
            suppliers,
            "ToStringFromLongEvaluator[lng=" + read + "]",
            DataType.KEYWORD,
            l -> new BytesRef(Long.toString(l)),
            Long.MIN_VALUE,
            Long.MAX_VALUE,
            List.of()
        );
        TestCaseSupplier.forUnaryUnsignedLong(
            suppliers,
            "ToStringFromUnsignedLongEvaluator[lng=" + read + "]",
            DataType.KEYWORD,
            ul -> new BytesRef(ul.toString()),
            BigInteger.ZERO,
            UNSIGNED_LONG_MAX,
            List.of()
        );
        TestCaseSupplier.forUnaryDouble(
            suppliers,
            "ToStringFromDoubleEvaluator[dbl=" + read + "]",
            DataType.KEYWORD,
            d -> new BytesRef(Double.toString(d)),
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            List.of()
        );
        TestCaseSupplier.forUnaryBoolean(
            suppliers,
            "ToStringFromBooleanEvaluator[bool=" + read + "]",
            DataType.KEYWORD,
            b -> new BytesRef(b.toString()),
            List.of()
        );
        TestCaseSupplier.unary(
            suppliers,
            "ToStringFromDatetimeEvaluator[datetime=" + read + "]",
            TestCaseSupplier.dateCases(),
            DataType.KEYWORD,
            i -> new BytesRef(DateFieldMapper.DEFAULT_DATE_TIME_FORMATTER.formatMillis(DateUtils.toLongMillis((Instant) i))),
            List.of()
        );
        TestCaseSupplier.unary(
            suppliers,
            "ToStringFromDateNanosEvaluator[datetime=" + read + "]",
            TestCaseSupplier.dateNanosCases(),
            DataType.KEYWORD,
            i -> new BytesRef(DateFieldMapper.DEFAULT_DATE_TIME_NANOS_FORMATTER.formatNanos(DateUtils.toLong((Instant) i))),
            List.of()
        );
        TestCaseSupplier.forUnaryGeoPoint(
            suppliers,
            "ToStringFromGeoPointEvaluator[wkb=" + read + "]",
            DataType.KEYWORD,
            wkb -> new BytesRef(GEO.wkbToWkt(wkb)),
            List.of()
        );
        TestCaseSupplier.forUnaryCartesianPoint(
            suppliers,
            "ToStringFromCartesianPointEvaluator[wkb=" + read + "]",
            DataType.KEYWORD,
            wkb -> new BytesRef(CARTESIAN.wkbToWkt(wkb)),
            List.of()
        );
        TestCaseSupplier.forUnaryGeoShape(
            suppliers,
            "ToStringFromGeoShapeEvaluator[wkb=" + read + "]",
            DataType.KEYWORD,
            wkb -> new BytesRef(GEO.wkbToWkt(wkb)),
            List.of()
        );
        TestCaseSupplier.forUnaryCartesianShape(
            suppliers,
            "ToStringFromCartesianShapeEvaluator[wkb=" + read + "]",
            DataType.KEYWORD,
            wkb -> new BytesRef(CARTESIAN.wkbToWkt(wkb)),
            List.of()
        );
        TestCaseSupplier.forUnaryIp(
            suppliers,
            "ToStringFromIPEvaluator[ip=" + read + "]",
            DataType.KEYWORD,
            ip -> new BytesRef(DocValueFormat.IP.format(ip)),
            List.of()
        );
        TestCaseSupplier.forUnaryStrings(suppliers, read, DataType.KEYWORD, bytesRef -> bytesRef, List.of());
        TestCaseSupplier.forUnaryVersion(
            suppliers,
            "ToStringFromVersionEvaluator[version=" + read + "]",
            DataType.KEYWORD,
            v -> new BytesRef(v.toString()),
            List.of()
        );
        return parameterSuppliersFromTypedDataWithDefaultChecksNoErrors(true, suppliers);
    }

    @Override
    protected Expression build(Source source, List<Expression> args) {
        return new ToString(source, args.get(0));
    }
}
