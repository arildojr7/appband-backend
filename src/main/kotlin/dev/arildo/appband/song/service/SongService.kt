package dev.arildo.appband.song.service

import dev.arildo.appband.musician.service.MusicianService
import dev.arildo.appband.song.model.Song
import dev.arildo.appband.song.repository.SongRepository
import dev.arildo.appband.song.service.dto.AddSongRequestDTO
import dev.arildo.appband.song.service.dto.SongResponseDTO
import org.bson.types.ObjectId
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class SongService(private val songRepository: SongRepository,
                  private val musicianService: MusicianService,
                  private val modelMapper: ModelMapper) {

    fun getSongs(): List<SongResponseDTO> {
        val resultList = mutableListOf<SongResponseDTO>()

        songRepository.findAll().forEach { song ->
            val singerName = musicianService.getMusicianById(song.singerId.toString())?.name

            resultList.add(
                    SongResponseDTO(
                            song.id,
                            song.title,
                            singerName,
                            song.tone,
                            song.capo,
                            song.thumb,
                            song.chord
                    )
            )
        }
        return resultList
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

        return SongResponseDTO(
                savedSong.id,
                savedSong.title,
                savedSong.singerId.toString(),
                savedSong.tone,
                savedSong.capo,
                savedSong.thumb,
                savedSong.chord
        )
    }

    fun getSongByIds(songIds: List<String>): List<SongResponseDTO> {
        return songRepository.findAllById(songIds).mapNotNull {
            it.toDTO()
        }
    }

    // region EXTENSIONS
    private fun Song.toDTO(): SongResponseDTO? {
        return modelMapper.map(this, SongResponseDTO::class.java)?.apply<SongResponseDTO?> {
            this?.singer = musicianService.getMusicianById(singerId.toString())?.name
        }
    }
    // endregion

}