import {Body, Controller, HttpCode, Post, UsePipes} from '@nestjs/common';
import { StatePostService } from '../services/state-post.service';
import {ExtractModelsPipe} from "../pipes/extract-models.pipe";
import {EntityMap} from "../models/entity-map";

@Controller('states')
export class StateController {

  constructor(private readonly statePostService: StatePostService) {}

  @Post()
  @HttpCode(201)
  @UsePipes(ExtractModelsPipe)
  async createState(@Body() entityMaps: Array<EntityMap>): Promise<any>{
    await this.statePostService.pushEntities(entityMaps);
    return 'State pushed to services successfully.';
  }
}
