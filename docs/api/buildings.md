# BuildingController — Tòa nhà

Base path: `/api/v1/buildings`

| Method | Path | Query/body | Data payload |
|---|---|---|---|
| GET | `/api/v1/buildings` | `page`, `size`, `wardId`, `sort` | `Page<BuildingResponse>` |
| GET | `/api/v1/buildings/search` | `keyword`, `provinceId`, `wardId`, `page`, `size`, `sort` | `Page<BuildingSearchResponse>` |
| GET | `/api/v1/buildings/nearby` | `latitude`, `longitude`, `radiusMeters`, `page`, `size` | `Page<BuildingSearchResponse>` |
| POST | `/api/v1/buildings` | `BuildingRequest` | `BuildingResponse`; HTTP 201 + `Location` |
| GET | `/api/v1/buildings/{buildingId}` | — | `BuildingResponse` |
| PUT | `/api/v1/buildings/{buildingId}` | `BuildingRequest` | `BuildingResponse` |
| DELETE | `/api/v1/buildings/{buildingId}` | — | `null` |

Mặc định `page=0`, `size=20`; yêu cầu `page >= 0`, `1 <= size <= 100`. List/search mặc định `sort=createdAt,desc`; thuộc tính hỗ trợ `createdAt`, `updatedAt`, `name`, hướng `asc`/`desc`. Search nhận keyword và lọc province/ward tùy chọn.

Nearby yêu cầu latitude [-90,90], longitude [-180,180], `radiusMeters > 0`; giới hạn bán kính mặc định 50.000m, chỉnh qua `BUILDING_SEARCH_MAX_RADIUS_METERS`. Sắp xếp mặc định gần nhất trước.

## BuildingRequest

```json
{"name":"Green Apartment","address":"123 Nguyễn Huệ","wardId":"898785309339156481","provinceId":"898785309339156482","latitude":10.7769,"longitude":106.7009,"description":"Căn hộ cho thuê"}
```

| Field | Kiểu | Bắt buộc | Ràng buộc |
|---|---|---:|---|
| `name` | string | Có | Không rỗng, tối đa 200 ký tự |
| `address` | string | Có | Không rỗng, tối đa 500 ký tự |
| `wardId`, `provinceId` | string số dương | Có | ID đơn vị hành chính |
| `latitude` | number | Có | -90 đến 90 |
| `longitude` | number | Có | -180 đến 180 |
| `description` | string/null | Không | — |

`BuildingResponse`: `buildingId` (string), `name`, `address`, `wardId` (string), `provinceId` (string), `latitude`, `longitude`, `description`, `createdAt`, `updatedAt`.

`BuildingSearchResponse` bổ sung `provinceName`, `wardName`, `distanceMeters` (có thể null) và `buildingUrlImage` (string/null). Trong kết quả `/search` và `/nearby`, `buildingUrlImage` là URL của ảnh đầu tiên của tòa nhà theo `display_order` tăng dần; nếu cùng thứ tự thì theo `building_image_id` tăng dần. Giá trị là `null` khi tòa nhà chưa có ảnh. Lỗi thường gặp: `400` input/page/sort, `404` building hoặc tham chiếu ward/province, `409` xóa building đang có phòng.
