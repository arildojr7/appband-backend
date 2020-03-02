package dev.arildo.appband.song.service

import dev.arildo.appband.singer.service.SingerService
import dev.arildo.appband.song.model.Song
import dev.arildo.appband.song.repository.SongRepository
import dev.arildo.appband.song.service.dto.AddSongRequestDTO
import dev.arildo.appband.song.service.dto.SongResponseDTO
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class SongService(private val songRepository: SongRepository,
                  private val singerService: SingerService) {

    fun getSongs(): List<SongResponseDTO> {
        val resultList = mutableListOf<SongResponseDTO>()

        songRepository.findAll().forEach { song ->
            val singerName = singerService.getSingerById(song.singerId.toString())?.name

            resultList.add(
                    SongResponseDTO(
                            song.id,
                            song.title,
                            singerName,
                            song.tone,
                            song.capo,
                            song.thumb,
                            HTML_PREFIX + song.chord + HTML_POSTFIX
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
                HTML_PREFIX + savedSong.chord + HTML_POSTFIX
        )
    }

    companion object {
        const val HTML_PREFIX = """<html><head>
<style type='text/css'>
b {
color: #f09227; 
font-weight: bold;
}
pre {
white-space: pre-wrap; 
word-break: break-word;
}
body {
margin:0;
padding:0;
color: #000000; 
font: 14px arial, sans-serif;
}
</style>
</head>
<body>"""
        const val HTML_POSTFIX = """</body>"""
    }
}