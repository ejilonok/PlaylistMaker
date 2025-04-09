package com.ejilonok.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlayerBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = PlayerViewModel(this, binding)
        playerViewModel.onCreate()
        playerViewModel.load(savedInstanceState)
    }

    override fun onDestroy() {
        playerViewModel.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        // TODO: Так как теперь модель не будет уничтожаться с активити, за сохранение состояние
        //  и загрузку будет отвечать модель - нужно убрать сохранение настроек в Bundle
        playerViewModel.save(outState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        playerViewModel.load(savedInstanceState)
    }
}