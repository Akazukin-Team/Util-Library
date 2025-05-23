package org.akazukin.util.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Sorter {
    public <T> List<T> mergeSort(final List<T> list, final Comparator<T> comparator) {
        final List<T> listA = new ArrayList<>(list.size() / 2);
        final List<T> listB = new ArrayList<>(list.size() - listA.size());
    }

    private <T> List<T> mergeSort(final List<T> list, final List<T> list2, final Comparator<T> comparator) {
    }
}


final class Main {
    public static void main(final String... args) {
        Sorter.mergeSort(Arrays.asList(new ClazzA(1), new ClazzA(2)),
                new Comparator<ClazzA>() {
                    @Override
                    public int compare(final ClazzA a, final ClazzA b) {
                        if (a.getI() > b.getI()) {
                            return 1;
                        } else if (a.getI() < b.getI()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
        ))
        Arrays.asList(new ClazzA(1), new ClazzA(2))
                .sort(new Comparator<ClazzA>() {
                          @Override
                          public int compare(final ClazzA a, final ClazzA b) {
                              if (a.getI() > b.getI()) {
                                  return 1;
                              } else if (a.getI() < b.getI()) {
                                  return -1;
                              } else {
                                  return 0;
                              }
                          }
                      }
                );
    }
}

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@AllArgsConstructor
final class ClazzA {
    int i;
}