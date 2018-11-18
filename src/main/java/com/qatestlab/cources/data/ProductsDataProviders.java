package com.qatestlab.cources.data;

import org.testng.annotations.DataProvider;

import java.util.Random;

public class ProductsDataProviders {
    private static final Random random = new Random();

    @DataProvider
    public static Object[][] newProduct() {
        return new Object[][] {
                {
                        "LiudmylaAProduct",
                        random.ints(1, 100).findFirst().getAsInt(),
                        random.doubles(0.1, 100).findFirst().getAsDouble()
                }
        };
    }
}
