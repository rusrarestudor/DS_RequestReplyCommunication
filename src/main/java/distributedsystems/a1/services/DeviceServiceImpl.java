package distributedsystems.a1.services;


import distributedsystems.a1.DTO.DeviceDTO;
import distributedsystems.a1.DTO.UserDTO;
import distributedsystems.a1.DTO.builder.DeviceBuilder;
import distributedsystems.a1.DTO.builder.UserBuilder;
import distributedsystems.a1.entities.Device;
import distributedsystems.a1.entities.User;
import distributedsystems.a1.repositories.DeviceRepo;

import distributedsystems.a1.repositories.UserRepo;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private DeviceRepo deviceRepo;

    @Autowired
    public DeviceServiceImpl(DeviceRepo deviceRepo) { this.deviceRepo = deviceRepo; }

    @Override
    public List<DeviceDTO> findDevices(){
        List<Device> deviceList = deviceRepo.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO findDeviceById(Long id){
        Optional<Device> deviceOptional = deviceRepo.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(deviceOptional.get());
    };


    @Override
    public Long insertDevice(DeviceDTO deviceDTO){
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepo.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getDeviceID());
        return device.getDeviceID();
    };
}
