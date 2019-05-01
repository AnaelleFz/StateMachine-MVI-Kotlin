package com.example.statemachine.model

import com.example.statemachine.view.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MainPresenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: MainActivity


    fun bind(view: MainActivity) {
        this.view = view
        compositeDisposable.add(observeAlertAction())
        compositeDisposable.add(observeStopAction())
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }


    private fun observeAlertAction() = view.displayAlertIntent()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { view.render(state = State.AlertState("AlertState")) }
        .subscribe()

    private fun observeStopAction() = view.displayStopIntent()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { view.render(state = State.StopState("StopState")) }
        .subscribe()

}
