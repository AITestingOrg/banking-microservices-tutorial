import { Module } from '@nestjs/common';
import { StateController } from './controllers/state.controller';
import { StatePostService } from './services/state-post.service';

@Module({
  imports: [],
  controllers: [StateController],
  providers: [StatePostService],
})
export class AppModule {}
