import { SingleActiveDebouncedAction } from '@/main/frontend/app/api/SingleActiveDebouncedAction';

describe('SingleActiveDebouncedAction', () => {
  it('should debounce and reject outdated actions', async () => {
    const executeAndtimeoutThis = jest.fn(() => wait(190));
    const executeAndResolveThis = jest.fn(
      () =>
        new Promise<void>((resolve) => {
          resolve();
        })
    );
    const debounceThis = jest.fn(() => wait(190));
    const catchCall = jest.fn();
    const thenCall = jest.fn();
    const singleActiveDebouncedAction = new SingleActiveDebouncedAction(100);

    singleActiveDebouncedAction.enqueueAction(debounceThis).then(thenCall).catch(catchCall);
    await wait(10);
    singleActiveDebouncedAction.enqueueAction(executeAndtimeoutThis).then(thenCall).catch(catchCall);
    await wait(120);
    singleActiveDebouncedAction.enqueueAction(executeAndResolveThis).then(thenCall).catch(catchCall);
    await wait(200);

    expect(debounceThis).toHaveBeenCalledTimes(0);
    expect(executeAndtimeoutThis).toHaveBeenCalledTimes(1);
    expect(executeAndResolveThis).toHaveBeenCalledTimes(1);
    expect(thenCall).toHaveBeenCalledTimes(1);
    expect(catchCall).toHaveBeenCalledTimes(2);
  });

  async function wait(milliseconds: number): Promise<void> {
    return new Promise<void>((resolve) => {
      setTimeout(() => resolve(), milliseconds);
    });
  }
});
