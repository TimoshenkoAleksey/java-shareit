package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final Valid valid;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    @Override
    public ItemRequestDto add(long userId, ItemRequestDto itemRequestDto) {
        User requester = valid.checkUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requester);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getByUserId(long userId) {
        valid.checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(userId);
        return addItemsInItemRequest(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        valid.checkUser(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNot(userId, pageRequest);
        if (itemRequests.isEmpty()) {
            return Collections.emptyList();
        }
        return addItemsInItemRequest(itemRequests);
    }

    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        valid.checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(format("Запрос с id = %s не найден", requestId)));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        List<ItemDto> itemDtoList = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
        if (!itemDtoList.isEmpty()) {
            itemRequestDto.setItems(itemDtoList);
        }
        return itemRequestDto;
    }

    private List<ItemRequestDto> addItemsInItemRequest(List<ItemRequest> itemRequests) {
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<ItemDto> itemsDto = itemRepository.findAllByRequestIdIn(itemRequestIds).stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        List<ItemRequestDto> requestsDto = itemRequests.stream().map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto : requestsDto) {
            itemRequestDto.setItems(itemsDto.stream()
                    .filter(i -> Objects.equals(i.getRequestId(), itemRequestDto.getId()))
                    .collect(Collectors.toList()));
        }
        return requestsDto;
    }
}
