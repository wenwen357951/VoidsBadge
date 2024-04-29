package com.wennest.yeemo.vbadge.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtil {
    @NotNull
    public static List<String> getByFirstLetters(@NotNull String args, @NotNull List<String> source) {
        List<String> result  = new ArrayList<>();
        List<String> suggest = new ArrayList<>(source);
        org.bukkit.util.StringUtil.copyPartialMatches(args, suggest, result);
        Collections.sort(result);
        return result;
    }
}
