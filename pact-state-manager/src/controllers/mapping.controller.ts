import {Body, Controller, Get, Post, Put} from '@nestjs/common';

@Controller('mapping')
export class MappingController {


    @Get()
    getMappings(): Array<any> {

    }

    @Put()
    updateMapping(@Body() mapping: any) {

    }

    @Post()
    createMapping(@Body() mapping: any) {

    }
}
