import { Test, TestingModule } from '@nestjs/testing';
import { StateController } from './state.controller';
import { StatePostService } from '../services/state-post.service';

describe('StateController', () => {
  let appController: StateController;

  beforeEach(async () => {
    const app: TestingModule = await Test.createTestingModule({
      controllers: [StateController],
      providers: [StatePostService],
    }).compile();

    appController = app.get<StateController>(StateController);
  });

  describe('root', () => {
    it('should return "Hello World!"', () => {
      expect(appController.getHello()).toBe('Hello World!');
    });
  });
});
