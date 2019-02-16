package meugeninua.android.currencies.model.data;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class PairLiveData<F, S> extends MediatorLiveData<Pair<F, S>> {

    private F first;
    private S second;

    public PairLiveData(final LiveData<F> firstLiveData, final LiveData<S> secondLiveData) {
        addSource(firstLiveData, this::onFirstValueChanged);
        addSource(secondLiveData, this::onSecondValueChanged);
    }

    private void onFirstValueChanged(final F first) {
        this.first = first;
        setValueIfNeeded();
    }

    private void onSecondValueChanged(final S second) {
        this.second = second;
        setValueIfNeeded();
    }

    private void setValueIfNeeded() {
        if (first == null || second == null) {
            return;
        }
        setValue(new Pair<>(first, second));
    }
}
