package com.oklab.opensources.dagger.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by andman on 2017-01-05.
 */
public class CollectionUtils {

    public <T> List<T> toSortedList(Comparator<T> comparator, T... stringsToSort) {
        List<T> list = Arrays.asList(stringsToSort);
        Collections.sort(list, comparator);
        return list;
    }
}
