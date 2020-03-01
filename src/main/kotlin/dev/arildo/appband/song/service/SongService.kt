package dev.arildo.appband.song.service

import dev.arildo.appband.song.model.Song
import dev.arildo.appband.song.repository.SongRepository
import dev.arildo.appband.song.service.dto.AddSongRequestDTO
import dev.arildo.appband.song.service.dto.SongResponseDTO
import org.bson.types.ObjectId
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.stereotype.Service

@Service
class SongService(private val songRepository: SongRepository,
                  private val modelMapper: ModelMapper) {
    fun getSongs(): List<SongResponseDTO> {
        val listType = object : TypeToken<List<SongResponseDTO>>() {}.type
        return modelMapper.map(songRepository.findAll(), listType)
    }

    fun addSong(request: AddSongRequestDTO): SongResponseDTO {
        val savedSong = songRepository.save(Song(
                title = request.title,
                singerId = ObjectId(request.singerId),
                tone = request.tone,
                capo = request.capo,
                thumb = request.thumb,
                chord = request.chord
        ))

        return SongResponseDTO(savedSong.id, savedSong.title, savedSong.singerId.toString(), savedSong.tone, savedSong.capo, savedSong.thumb, savedSong.chord)
    }
}