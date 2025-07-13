/**
 * Klasse mit deren Instanzen asynchrone Aktionen gescheduled werden.
 * Die Aktionen werden debounced und zusätzlich wird sichergestellt das,
 * wenn mehrere Aktionen gleichzeitig aktiv sind, nur diejenige resolved die als letztes enqued wurde
 *
 * @typeParam T Der Rückgabetyp der Asynchronen Action die enqued werden soll
 */
export class SingleActiveDebouncedAction<T> {
  private debounceWaitTime: number;
  private lastActionNumber = 0;
  private currentDebounceTimer: NodeJS.Timeout | undefined = undefined;
  private currentDebounceTimerRejectOption: null | ((reason: string) => void) = null;
  private actiondescription: string;

  /**
   *
   * @param debounceTime Time in milliseconds that this instance waits before starting the action as to debounce it
   * @param actiondescription Optional description of the action that improves the messages generated when an action is canceled.
   */
  constructor(debounceTime: number, actiondescription = 'Action') {
    this.debounceWaitTime = debounceTime;
    this.actiondescription = actiondescription;
  }

  /**
   *
   * @param action Die Aktion die debounced ausgeführt werden soll
   */
  public enqueueAction(action: () => Promise<T>): Promise<T> {
    this.clearExistingTimer();
    this.rejectPreviousActionBecauseOfDebounce();

    return new Promise<T>((resolve, reject) => {
      this.currentDebounceTimerRejectOption = reject;
      this.currentDebounceTimer = this.createDebouncedExecution(action, resolve, reject);
    });
  }

  private clearExistingTimer() {
    if (this.currentDebounceTimer) {
      clearTimeout(this.currentDebounceTimer);
    }
  }

  private rejectPreviousActionBecauseOfDebounce() {
    if (this.currentDebounceTimerRejectOption) {
      this.currentDebounceTimerRejectOption(this.actiondescription + ' discarded because of debounce');
    }
  }

  private isThisActionNumberTheMostCurrent(actionnumber: number): boolean {
    return this.lastActionNumber === actionnumber;
  }

  private createDebouncedExecution(
    action: () => Promise<T>,
    resolveOption: (value: T | PromiseLike<T>) => void,
    rejectOption: (reason?: any) => void
  ) {
    return (this.currentDebounceTimer = setTimeout(async () => {
      this.currentDebounceTimerRejectOption = null;

      action().then(this.getActionResultHandler(resolveOption, rejectOption)).catch(rejectOption);
    }, this.debounceWaitTime));
  }

  private getActionResultHandler(
    resolveOption: (value: T | PromiseLike<T>) => void,
    rejectOption: (reason?: any) => void
  ) {
    const thisActionNumber = this.getNewActionNumber();
    return (response: T) => {
      if (this.isThisActionNumberTheMostCurrent(thisActionNumber)) {
        resolveOption(response);
      } else {
        rejectOption(this.actiondescription + ' discarded because a newer Action was started');
      }
    };
  }

  private getNewActionNumber(): number {
    //Invalidate old actionnumber
    this.lastActionNumber++;
    return this.lastActionNumber;
  }
}
