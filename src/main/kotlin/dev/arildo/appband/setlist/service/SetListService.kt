package dev.arildo.appband.setlist.service

import dev.arildo.appband.musician.service.MusicianService
import dev.arildo.appband.setlist.model.SetList
import dev.arildo.appband.setlist.repository.SetListRepository
import dev.arildo.appband.setlist.service.dto.AddSetListRequestDTO
import dev.arildo.appband.setlist.service.dto.SetListDTO
import dev.arildo.appband.song.service.SongService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.*

@Service
class SetListService(private val setListRepository: SetListRepository,
                     private val songService: SongService,
                     private val musicianService: MusicianService) {

    fun addSetList(request: AddSetListRequestDTO): SetListDTO {
        val setListToSave = SetList(
                null,
                Date(request.date),
                request.musicians.map { ObjectId(it) },
                request.songs.map { ObjectId(it) }
        )

        return setListRepository.save(setListToSave).toDTO()
    }

    fun getSetLists(): List<SetListDTO> {
        return setListRepository.findAll().mapIndexed { index, setList -> setList.toDTO(index) }
    }

    // region EXTENSIONS

    private fun SetList.toDTO(number: Int? = null): SetListDTO {
        return SetListDTO(
                this.id,
                number,
                this.date?.time,
                this.musicianIds.run {
                    musicianService.getMusicianByIds(this.map { it.toString() })
                },
                this.songIds.run {
                    songService.getSongByIds(this.map { it.toString() })
                }
        )
    }

    // endregion
}