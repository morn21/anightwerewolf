package online.morn.anightwerewolf.controller.json;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/room")
@RestController
public class RoomController {
    private Logger logger = Logger.getLogger(RoomController.class);
}
