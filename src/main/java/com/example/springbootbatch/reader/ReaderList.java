package com.example.springbootbatch.reader;

import com.example.springbootbatch.model.Car;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.Iterator;

public class ReaderList implements ItemReader<Car> {

    Iterator<Car> carsIter = new ArrayList<Car>() {{
        add(new Car(0, "Honda", "Civic", 1997));
        add(new Car(1, "Honda", "Accord", 2003));
        add(new Car(2, "Ford", "Escort", 1985));
    }}.iterator();

    @BeforeRead
    public void beforeRead() {
        System.out.println("Before read (annotation) ...");
    }

    @AfterRead
    public void afterRead(Object item) {
        System.out.println("After read (annotation) ...");
    }

    @OnReadError
    public void onReadError(Exception ex) {
        System.out.println("On read error (annotation) ...");
    }

    @Override
    public Car read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return carsIter.hasNext() ? carsIter.next() : null;
    }
}
