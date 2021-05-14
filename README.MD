# DataFrame

DataFrame is light-weight data structure for working with tabular data. It's primary focus is to be a counterpart for
DataViz and allow for easy creation of plots. Additionally, it includes various statistical methods to enable better
data exploration.

## Importing a CSV file

Importing data can be performed using a static factory method in the DataFrame interface:

```java

import net.mahdilamb.dataframe.DataFrame;

import java.io.File;

public class Tests {
    public static DataFrame loadFromResources(final String name) {
        return DataFrame.from(new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(name)).getFile()));
    }

    public static void main(String[] args) {
        DataFrame iris = DataFrame.from(new File("iris.csv"));
    }

}

```

Printing to the console will print in a friendly way.

# DataFrame methods

Many common table methods can be performed including:

* sorting
* filter
* grouping
* subsetting

# Stats/Maths

There are also a number of useful utility methods for working with numerical data. These can be found in
the `net.mahdilamb.stats` package and contains methods to summarise data as well as methods for working with arrays.
There are also various methods for working the density estimations.

# Attributions

* Many mathematical functions have been translated from the [Cephes](https://www.netlib.org/cephes/) math library which
  is released with an [MIT Licence](https://en.smath.com/view/CephesMathLibrary/license)
* There are also mathematical methods derived from [NumPy](https://numpy.org/doc/stable/license.html)
  and [SciPy](https://www.scipy.org/scipylib/license.html)
* Finally, there are also some methods derived from [Boost](https://www.boost.org/users/license.html)

# Licence

Copyright (C) 2020-2021 Mahdi Lamb, University of Edinburgh

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "
AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.