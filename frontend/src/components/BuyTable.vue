<template>
    <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="flightID" label="航班编号" width="180">
        </el-table-column>
        <el-table-column prop="flightCompany" label="航空公司" width="180">
        </el-table-column>
        <el-table-column prop="departure" label="出发地" width="180">
        </el-table-column>
        <el-table-column prop="departureTime" label="出发时间" width="180">
        </el-table-column>
        <el-table-column prop="arrival" label="到达地" width="180">
        </el-table-column>
        <el-table-column prop="arrivalTime" label="到达时间" width="180">
        </el-table-column>
    </el-table>
</template>

<script>
    export default {
        name: 'BuyTable',
        data: function () {
            return {
                tableData: []
            }
        },
        mounted() {
            this.getData();
        },
        methods: {
            getData() {
                const that = this;
                axios.get('api/getFlightInCart', {}).then(function (ret) {
                    console.log(ret)
                    if (ret.data.status === 0) {
                        that.tableData = ret.data.data;
                    }
                }, function () {
                    console.log('传输失败');
                });
            }
        }
    }
</script>