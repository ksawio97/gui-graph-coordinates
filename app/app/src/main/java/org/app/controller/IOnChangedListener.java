package org.app.controller;

@FunctionalInterface
public interface IOnChangedListener<T> {
    void onChanged(T eventData);
}
