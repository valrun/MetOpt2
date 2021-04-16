package com.example.metopt;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.metopt.nmethods.QuadraticFunction;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.R)
public class SaveFunction {
    public static QuadraticFunction function = new QuadraticFunction(
            List.of(List.of(20.0, 2.0), List.of(2.0, 3.0)),
            List.of(-5.0, 3.0),
            2.0
    );
}
