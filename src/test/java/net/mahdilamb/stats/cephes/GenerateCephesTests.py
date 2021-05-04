from __future__ import annotations

import math
import os
from scipy import special
from typing import NamedTuple, Callable

output = "net.mahdilamb.statistics.cephes.JUnitTests.java"
cephes = "net.mahdilamb.statistics.libs.Cephes"


class TestCase:
    def generate_test_case(self) -> str:
        raise Exception(self.__class__.__name__ + " does not override the generate_test_case method")

    @staticmethod
    def format_double(val: float) -> str:
        TestGenerator._usesDouble = True
        """Format a float as a Java double"""
        return "Double.NaN" if math.isnan(val) else str(val) if not math.isinf(
            val) else "Double.POSITIVE_INFINITY" if val > 0 else "Double.NEGATIVE_INFINITY"

    @staticmethod
    def format_float(val: float) -> str:
        TestGenerator._usesFloat = True
        """Format a float as a Java float"""
        return "Float.NaN" if math.isnan(val) else str(val) if not math.isinf(
            val) else "Float.POSITIVE_INFINITY" if val > 0 else "Float.NEGATIVE_INFINITY"


class TestCaseDoubleUnary(NamedTuple, TestCase):
    """Test case for use in generating tests
    :argument
    test_name: the name of the output method. Must not be the a method name in the Cephes
    method: the name of the Cephes method
    cases: a list of floats to test against
    for_each: a function reference to a scipy function (if multi-arg, may need to be a lambda function)
    """
    test_name: str
    method: str
    cases: list[float]
    for_each: Callable[[float], float]

    def generate_test_case(self) -> str:
        """Generate a test method"""
        out = "\t/**\n"
        out += "\t * Run auto-generated %s\n" % self.test_name
        out += "\t*/\n"
        out += "\t@Test\n"

        out += "\tpublic void %s(){\n" % self.test_name
        for i in self.cases:
            out += "\t\tassertEquals(%s, %s(%s), precision);\n" % (
                TestCase.format_double(self.for_each(i)), self.method, TestCase.format_double(i))
        out += "\t}\n\n"
        return out;


class TestCaseFloatUnary(NamedTuple, TestCase):
    """Test case for use in generating tests
    :argument
    test_name: the name of the output method. Must not be the a method name in the Cephes
    method: the name of the Cephes method
    cases: a list of floats to test against
    for_each: a function reference to a scipy function (if multi-arg, may need to be a lambda function)
    """
    test_name: str
    method: str
    cases: list[float]
    for_each: Callable[[float], float]

    def generate_test_case(self) -> str:
        """Generate a test method"""
        out = "\t/**\n"
        out += "\t * Run auto-generated %s\n" % self.test_name
        out += "\t*/\n"
        out += "\t@Test\n"

        out += "\tpublic void %s(){\n" % self.test_name
        for i in self.cases:
            out += "\t\tassertEquals(%s, %s(%s), precision);\n" % (
                TestCase.format_float(self.for_each(i)), self.method, TestCase.format_float(i))
        out += "\t}\n\n"
        return out;


class TestGenerator:
    _usesFloat = False;
    _usesDouble = False;
    """Generate test cases of methods against scipy to ensure consistency between libraries
    :argument
    cephes: the path to the cephes package
    output: the output file
    precision: a string representation of the double-precision precision (i.e. what is the allowable difference between the Java version and scipy version
    tests: a list of test_cases to work against
    """

    def __init__(self, cephes: str, output: str, precision: str, tests: list[TestCase]) -> None:
        self._cephes = cephes;
        self._output = output;
        self._precision = precision;
        self._tests = tests;

    def run(self):
        """Generate the output and save to the output path"""
        out_split = self._output.split(".")

        # check directory
        out_copy = out_split[:-2]
        s = t = os.path.dirname(os.path.realpath(__file__))
        while t is not None and t != "" and len(out_copy):
            s, t = os.path.split(s)
            if out_copy.pop() != t:
                raise Exception(
                    "Using incorrect working directory. Must run python from the directory of the output file");
        with open(".".join(out_split[-2:]), 'w') as test_file:
            test_file.write("package %s;\n\n" % ".".join(out_split[:-2]));
            test_file.write("import static %s.*;\n" % self._cephes);
            test_file.write("import static org.junit.jupiter.api.Assertions.assertEquals;\n\n");
            test_file.write("import org.junit.jupiter.api.Test;\n");
            if TestGenerator._usesDouble:
                test_file.write("import java.lang.Double;\n\n");
            if TestGenerator._usesFloat:
                test_file.write("import java.lang.Float;\n\n");
            test_file.write("public final class %s {\n" % out_split[-2]);
            test_file.write("\tprivate static final double precision = %s;\n\n" % self._precision);

            for t in self._tests:
                assert t.method != t.test_name;
                for line in t.generate_test_case():
                    test_file.write(line);
            test_file.write("}")


if __name__ == "__main__":
    range_1d = [float('nan'), float("-inf"), -100, -10, -5, -1, -0.9, -.75, -.5, -.25, -0.1, -0.01, 0, 0.01, 0.1, .25,
                .5, .75, 0.9, 1, 5, 10, 100, float("inf")]
    TestGenerator(cephes, output, "0.00000001",
                  [
                      TestCaseDoubleUnary("ndtriTest", "ndtri", range_1d, special.ndtri),
                      TestCaseDoubleUnary("zetacTest", "zetac", range_1d,
                                          special.zetac),
                      TestCaseDoubleUnary("zetaTest", "riemann_zeta",
                                          range_1d,
                                          special.zeta),
                      TestCaseDoubleUnary("erfTest", "erf", range_1d,
                                          special.erf),

                      TestCaseDoubleUnary("erfcTest", "erfc", range_1d,
                                          special.erfc),

                      TestCaseDoubleUnary("gammaTest", "Gamma", range_1d,
                                          special.gamma),
                      TestCaseDoubleUnary("dawsnTest", "dawsn", range_1d,
                                          special.dawsn),
                      TestCaseDoubleUnary("ellipeTest", "ellpe", range_1d,
                                          special.ellipe),
                      TestCaseDoubleUnary("ellipkTest", "ellipk", range_1d,
                                          special.ellipk),
                  ]).run()
