package dev.arildo.appband.musician.service

import dev.arildo.appband.musician.model.Musician
import dev.arildo.appband.musician.repository.MusicianRepository
import dev.arildo.appband.musician.service.dto.AddMusicianRequestDTO
import dev.arildo.appband.musician.service.dto.MusicianDTO
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MusicianService(private val musicianRepository: MusicianRepository,
                      private val modelMapper: ModelMapper) {

    fun getMusicians(): List<MusicianDTO> {
        val listType = object : TypeToken<List<MusicianDTO>>() {}.type
        return modelMapper.map(musicianRepository.findAll(), listType)
    }

    fun getMusicianById(musicianId: String?): Musician? {
        return musicianRepository.findByIdOrNull(musicianId)
    }

    fun getMusicianByIds(musicianIds: List<String>): List<MusicianDTO> {
        return musicianRepository.findAllById(musicianIds).mapNotNull {
            it.toDTO()
        }
    }

    fun addMusician(request: AddMusicianRequestDTO): MusicianDTO {
        val savedMusician = musicianRepository.save(modelMapper.map(request, Musician::class.java))
        return modelMapper.map(savedMusician, MusicianDTO::class.java)
    }

    // region EXTENSIONS
    private fun Musician.toDTO(): MusicianDTO {
        return modelMapper.map(this, MusicianDTO::class.java)
    }
    // endregion

}