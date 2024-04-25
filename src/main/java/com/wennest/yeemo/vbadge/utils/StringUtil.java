package com.wennest.yeemo.vbadge.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtil {
    @NotNull
    public static List<String> getByFirstLetters(@NotNull String arg, @NotNull List<String> source) {
        List<String> ret  = new ArrayList<>();
        List<String> sugg = new ArrayList<>(source);
        org.bukkit.util.StringUtil.copyPartialMatches(arg, sugg, ret);
        Collections.sort(ret);
        return ret;
    }
}
