<template>
  <div>
    <div class="d-flex flex-row">
      <v-icon
        size="24"
        class="pl-4">
        far fa-clock
      </v-icon>
      <date-picker
        v-model="startScheduleDate"
        :min-value="minimumStartDate"
        class="scheduleStartDatePicker pa-0 pl-4" />
      <time-picker
        v-model="startScheduleTime"
        :min="minimumStartTime"
        class="mb-3" />
    </div>
    <div class="d-flex flex-row pl-8">
      <date-picker
        v-model="endScheduleDate"
        :min-value="minimumEndDate"
        class="scheduleEndDatePicker pa-0 pl-6" />
      <time-picker
        v-model="endScheduleTime"
        :min="minimumEndTime"
        class="mb-3" />
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    startScheduleDate: new Date(),
    startScheduleTime: new Date(new Date().getTime() + 900000),
    endScheduleDate: new Date(),
    endScheduleTime: new Date(new Date().getTime() + 1800000),
  }),
  computed: {
    minimumStartDate() {
      return new Date();
    },
    minimumStartTime() {
      if (!this.startScheduleDate || !this.checkDatesOnSameDay(this.startScheduleDate, new Date().getTime())) {
        return null;
      }
      return new Date();
    },
    minimumEndDate() {
      if (!this.startScheduleDate) {
        return null;
      }
      return new Date(this.startScheduleDate);
    },
    minimumEndTime() {
      this.$emit('change', this.startScheduleDate, this.endScheduleDate, this.startScheduleTime, this.endScheduleTime);
      if (!this.startScheduleDate || !this.endScheduleDate || !this.startScheduleTime || !this.endScheduleTime || !this.checkDatesOnSameDay(this.startScheduleDate, new Date().getTime())) {
        return null;
      }
      if (this.checkDatesOnSameDay(this.startScheduleDate, this.endScheduleDate)){
        return new Date(this.startScheduleTime.getTime() + 900000);
      }
      return null ;
    },
  },
  watch: {
    startScheduleDate(newVal, oldVal) {
      if (!newVal || !oldVal || new Date(newVal).getTime() === new Date(oldVal).getTime()) {
        return;
      }
      const newDate = new Date(newVal);
      this.endScheduleDate = new Date(newDate.getTime());
      if (this.checkDatesOnSameDay(newVal, new Date().getTime())) {
        this.startScheduleTime = new Date(new Date().getTime() + 900000);
      }
    },
    startScheduleTime(newVal, oldVal) {
      if (!newVal || !oldVal || new Date(newVal).getTime() === new Date(oldVal).getTime()) {
        return;
      }
      const newDate = new Date(newVal);
      this.endScheduleTime = new Date(newDate.getTime() + 900000);
    },
  },
  methods: {
    checkDatesOnSameDay(firstDate, secondDate) {
      return new Date(firstDate).getFullYear() === new Date(secondDate).getFullYear() &&
            new Date(firstDate).getMonth() === new Date(secondDate).getMonth() &&
            new Date(firstDate).getDate() === new Date(secondDate).getDate();
    },
  }
};
</script>