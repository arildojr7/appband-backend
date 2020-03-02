package dev.arildo.appband.singer.service

import dev.arildo.appband.singer.model.Singer
import dev.arildo.appband.singer.repository.SingerRepository
import dev.arildo.appband.singer.service.dto.AddSingerRequestDTO
import dev.arildo.appband.singer.service.dto.SingerDTO
import org.bson.types.ObjectId
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SingerService(private val singerRepository: SingerRepository,
                    private val modelMapper: ModelMapper) {

    fun getSingers(): List<SingerDTO> {
        val listType = object : TypeToken<List<SingerDTO>>() {}.type
        return modelMapper.map(singerRepository.findAll(), listType)
    }

    fun getSingerById(singerId: String?): Singer? {
        return singerRepository.findByIdOrNull(singerId)
    }

    fun addSinger(request: AddSingerRequestDTO): SingerDTO {
        val savedSinger = singerRepository.save(modelMapper.map(request, Singer::class.java))
        return modelMapper.map(savedSinger, SingerDTO::class.java)
    }

}