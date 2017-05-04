package com.xunshan.rxhelper.rx;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static org.mockito.Mockito.when;

/**
 * Created by eldorado on 17-5-4.
 *
 * flat map flow
 * 1. define function
 * Func1<? super T, ? extends Observable<? extends R>> func
 *        first arg      return type
 * 
 * 2. func accept an item then return observable
 * 3. merging this observable
 */
@RunWith(MockitoJUnitRunner.class)
public class FlatMapTest {
    private List<List<String>> source;
    private File[] folders;

    @Before
    public void setUp() throws Exception {
        initSource();
        initFolders();
    }

    @Mock
    File mockFolder;

    private void initFolders() {
        File[] subFolders = new File[]{mockFolder};
        File[] subFolders2 = new File[]{mockFolder, mockFolder};

        folders = new File[]{mockFolder, mockFolder};

        when(mockFolder.listFiles())
                .thenReturn(subFolders)
                .thenReturn(subFolders2);

        when(mockFolder.getName())
                .thenReturn("file1")
                .thenReturn("file2")
                .thenReturn("file3");
    }

    @Test
    public void flatStringListsIntoSingleList() {
        Observable.from(source)
                .flatMap(new Func1<List<String>, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(List<String> strings) {
                        if (strings.size() == 0) return null;
                        return Observable.just(strings);
                    }
                })
                .subscribe(System.out::println);
    }
    
    @Test
    public void extractFilesInDirsIntoList() {
        Observable.from(folders)
                .flatMap(file -> Observable.from(file.listFiles()))
                .map(File::getName)
                .subscribe(System.out::println);
    }

    private void initSource() {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");

        List<String> list2 = new ArrayList<>();
        list1.add("d");
        list1.add("e");
        list1.add("f");

        source = new ArrayList<>();
        source.add(list1);
        source.add(list2);
    }

}
